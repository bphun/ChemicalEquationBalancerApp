import React from 'react'
import { AppProvider, Page, Card, Image, Link, TextField, Stack, Modal, Badge } from "@shopify/polaris";
import "../App.css"
import queryString from 'query-string';
import { withRouter } from "react-router";
require('dotenv').config(process.env.NODE_ENV === "development" ? "../../.env.development" : "../../.env.production")

class RequestViewPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            shouldDisplayEditValueModal: false,
            editTargetValueId: "",
            modalTextInputValue: "",
            storedRequest: undefined,
            viewInfoReady: false,
            shouldDisplay: false,
            previousRequestId: undefined,
            nextRequestId: undefined,
            hasNextRequest: false,
            hasPreviousRequest: false
        }

        this.apiHostname = process.env.REACT_APP_API_HOSTNAME
    }

    componentDidMount() {
        const requestId = queryString.parse(this.props.location.search).rid

        fetch(this.apiHostname + "/storedRequests/view?rid=" + requestId, { mode: "cors" })
            .then(results => {
                return results.json()
            }).then(response => {
                this.setState({ storedRequest: response, shouldDisplay: true }, () => {
                    this.fetchNextAndPreviousRequestInfo()
                })
            }).catch(err => {
                console.log(err);
            })

    }

    fetchNextAndPreviousRequestInfo() {
        if (!this.state.nextRequestId) {
            fetch(this.apiHostname + "/storedRequests/nextRequest?t=" + this.state.storedRequest.gcpRequestStartTimeMs, { mode: "cors" })
                .then(results => {
                    return results.json()
                }).then(response => {
                    if (response.status !== "error") {
                        this.setState({ nextRequestId: response.id, hasNextRequest: true })
                    }
                }).catch(err => {
                    console.log(err);
                })
        }
        if (!this.state.previousRequestId) {
            fetch(this.apiHostname + "/storedRequests/previousRequest?t=" + this.state.storedRequest.gcpRequestStartTimeMs, { mode: "cors" })
                .then(results => {
                    return results.json()
                }).then(response => {
                    if (response.status !== "error") {
                        this.setState({ previousRequestId: response.id, hasPreviousRequest: true })
                    }
                }).catch(err => {
                    console.log(err);
                })
        }
    }

    handleValueEditModalSubmit() {
        if (this.state.editTargetValueId === "" || this.state.modalTextInputValue === "") { return }
        let url = this.apiHostname + "/storedRequests/updateValue?rid=" +
            this.state.storedRequest.id + "&vid=" + this.state.editTargetValueId +
            "&v=" + encodeURIComponent(this.state.modalTextInputValue)
        fetch(url)
            .then(results => {
                return results.json()
            }).then(response => {

                this.setState({ shouldDisplayEditValueModal: false, editTargetValueId: "" })

                if (this.state.editTargetValueId === "userInputtedChemicalEquationString") {
                    this.updateUserEquationString(this.state.modalTextInputValue)
                } else if (this.state.editTargetValueId === "verifiedChemicalEquationString") {
                    this.updateVerifiedEquationString(this.state.modalTextInputValue)
                }
            }).catch(err => {
                console.log(err);
            })
        window.location.reload(); // Yes i know very nice
    }

    updateVerifiedEquationString(str) {
        this.setState({
            ...this.state.storedRequest,
            verifiedChemicalEquationString: str
        })
    }

    updateUserEquationString(str) {
        this.setState({
            ...this.state.storedRequest,
            userInputtedChemicalEquationString: str
        })
    }

    onNextPageClick() {
        this.props.history.push({
            pathname: "/requests/view?rid=" + this.state.nextRequestId,
            state: { previousRequestId: this.state.storedRequest.id }
        })
        window.location.reload();
    }

    onPreviousPageClick() {
        this.props.history.push({
            pathname: "/requests/view?rid=" + this.state.previousRequestId,
            state: { nextRequestId: this.state.storedRequest.id }
        })
        window.location.reload();
    }

    deleteRequest() {
        fetch(this.apiHostname + "/storedRequests/deleteRequest?rid=" + this.state.storedRequest.id, { mode: "cors" })
            .then(results => {
                return results.json()
            })
            .then(response => {
                console.log(response)
                if (response.status === "success") {
                    this.props.history.push({ pathname: "/requests/list" })
                    window.location.reload();
                } else {
                    alert("Error: " + response.description);
                }
            })
            .catch(err => {
                console.log(err);
            })
    }

    showImageLabelingView() {
        console.log(this.state.storedRequest.s3ImageUrl)
        this.props.history.push({
            pathname: "/requests/label?rid=" + this.state.storedRequest.id + "&imgUrl=" + encodeURI(this.state.storedRequest.s3ImageUrl),
            // state: { imageUrl: this.state.storedRequest.s3ImageUrl, requestId: this.state.storedRequest.id }
        })
        window.location.reload();
    }

    render() {
        return !this.state.shouldDisplay ? <div></div> : this.displayView();
    }

    displayView() {
        let imageStyle = {
            flex: 1,
            width: 450,
            height: 700,
            resizeMode: 'contain'
        };

        const idMappings = {
            "verifiedChemicalEquationString": "verified equation string",
            "userInputtedChemicalEquationString": "user chemical equation string"
        }

        const request = this.state.storedRequest
        const pageTitle = "Image Review (" + request.id + ")"
        const requestDate = new Date(request.gcpRequestStartTimeMs)

        var requestStatusBadge;
        if (request.labelingStatus === "INCOMPLETE") {
            requestStatusBadge = <Badge status="attention" progress="incomplete">Incomplete</Badge>
        } else if (request.labelingStatus === "IN_PROGRESS") {
            requestStatusBadge = <Badge status="info" progress="partiallyComplete">In Progress</Badge>
        } else if (request.labelingStatus === "LABELED") {
            requestStatusBadge = <Badge status="success" progress="complete">Labeled</Badge>
        } else {
            requestStatusBadge = <Badge status="warning">Unavailable</Badge>
        }

        return (
            <React.Fragment>
                <head>
                    <title>Image Labeling Portal</title>
                    <meta charSet="utf-8" />
                    <link rel="stylesheet" href="https://unpkg.com/@shopify/polaris@4.11.0/styles.css" />
                </head>
                <AppProvider>
                    <Page
                        title={pageTitle}
                        fullWidth={true}
                        fullHeight={true}
                        breadcrumbs={[{ content: "List", url: "/requests/list" }]}
                        pagination={{
                            hasNext: this.state.hasNextRequest,
                            onNext: () => this.onNextPageClick(),
                            hasPrevious: this.state.hasPreviousRequest,
                            onPrevious: () => this.onPreviousPageClick()
                        }}>
                        <Modal
                            open={this.state.shouldDisplayEditValueModal}
                            onClose={() => this.setState({ shouldDisplayEditValueModal: false, editTargetValueId: "" })}
                            title="Edit Value"
                            primaryAction={{
                                content: "Submit",
                                onAction: () => this.handleValueEditModalSubmit(request),
                            }}
                        >
                            <Modal.Section>
                                <TextField
                                    value={this.state.modalTextInputValue}
                                    onChange={(value) => this.setState({ modalTextInputValue: value })}
                                    label={"Edit " + idMappings[this.state.editTargetValueId]}
                                />
                            </Modal.Section>
                        </Modal>
                        <Stack>
                            <Stack.Item>
                                <Card>

                                    <Image source={request.s3ImageUrl} style={imageStyle} />
                                </Card>
                            </Stack.Item>
                            <Stack.Item>
                                <Card
                                    title="Request Info"
                                    fullHeight={true}
                                    primaryFooterAction={
                                        {
                                            content: "Label Image",
                                            onAction: () => this.showImageLabelingView()
                                        }
                                    }
                                    secondaryFooterActions={
                                        [{
                                            content: "Delete Request",
                                            destructive: true,
                                            onAction: () => this.deleteRequest()
                                        }]
                                    }
                                >
                                    <Card.Section title="Execution Date">
                                        <p>{requestDate.toLocaleDateString("en-US") + " " + requestDate.toLocaleTimeString("en-US")}</p>
                                    </Card.Section>
                                    <Card.Section title="GCP Execution Time (ms)">
                                        <p>{request.gcpRequestEndTimeMs - request.gcpRequestStartTimeMs}</p>
                                    </Card.Section>
                                    <Card.Section title="GCP Equation String">
                                        <p>{request.gcpIdentifiedChemicalEquationString ? request.gcpIdentifiedChemicalEquationString : "Unavailable"}</p>
                                    </Card.Section>
                                    <Card.Section title="OD Execution Time (ms)">
                                        <p>{request.onDeviceImageProcessEndTime - request.onDeviceImageProcessStartTime}</p>
                                    </Card.Section>
                                    <Card.Section title="Labeling Status">
                                        {requestStatusBadge}
                                    </Card.Section>
                                    <Card.Section title="Verified Equation String" actions={[{ content: "Edit", onAction: () => this.setState({ shouldDisplayEditValueModal: true, editTargetValueId: "verifiedChemicalEquationString" }) }]}>
                                        <p>{request.verifiedChemicalEquationString ? decodeURI(request.verifiedChemicalEquationString) : "Unavailable"}</p>
                                    </Card.Section>
                                    <Card.Section title="User Equation String" actions={[{ content: "Edit", onAction: () => this.setState({ shouldDisplayEditValueModal: true, editTargetValueId: "userInputtedChemicalEquationString" }) }]}>
                                        <p>{request.userInputtedChemicalEquationString ? decodeURI(request.userInputtedChemicalEquationString) : "Unavailable"}</p>
                                    </Card.Section>
                                    <Card.Section title="S3 Image URL">
                                        <Link url={request.s3ImageUrl}> Link </Link>
                                    </Card.Section>
                                </Card>
                            </Stack.Item>
                        </Stack>
                    </Page>
                </AppProvider>
            </React.Fragment>
        );
    }
}

export default withRouter(RequestViewPage)