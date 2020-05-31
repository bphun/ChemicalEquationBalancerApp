import React from 'react'
import { AppProvider, Page, Card, Image, Link, TextField, Stack, Modal, Badge, Spinner } from "@shopify/polaris";
import "../App.css"
import { formatUrl } from "../Utility/Utility";
import queryString from 'query-string';
import { withRouter } from "react-router";
import { CircleLeftMajorMonotone, CircleRightMajorMonotone, WandMajorMonotone, EditMajorMonotone, FolderDownMajorMonotone } from '@shopify/polaris-icons';
import LoadingView from './LoadingView';
import Auth from '../Utility/Auth';
import config from 'react-global-configuration';

class RequestViewPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            processingRegions: false,
            shouldDisplayEditValueModal: false,
            editActionTargetRegion: "",
            modalTextInputValue: "",
            storedRequest: undefined,
            regions: undefined,
            viewInfoReady: false,
            shouldDisplay: false,
            previousRequestId: undefined,
            nextRequestId: undefined,
            hasNextRequest: false,
            hasPreviousRequest: false
        }

        this.apiUrl = config.get("apiUrl")
        this.authToken = Auth.getAuthToken()
    }

    componentDidMount() {
        const requestId = queryString.parse(this.props.location.search).rid

        fetch(formatUrl(this.apiUrl + "/requests/info", { rid: requestId }), {
            mode: "cors",
            headers: {
                "Authorization": `Bearer ${this.authToken}`
            }
        })
            .then(results => {
                return results.json()
            }).then(response => {
                this.setState({ storedRequest: response }, () => {
                    this.fetchNextAndPreviousRequestInfo()
                    this.getRegions(response.id)
                })
            }).catch(err => {
                console.error(err);
            })

    }

    getRegions(requestId) {
        let imageRegions = []
        fetch(this.apiUrl + "/regions/?rid=" + requestId, {
            mode: "cors",
            headers: {
                "Authorization": `Bearer ${this.authToken}`
            }
        })
            .then(results => {
                return results.json()
            })
            .then(fetchedBoundingBoxes => {
                for (let i in fetchedBoundingBoxes) {
                    let currResponse = fetchedBoundingBoxes[i]
                    let boundingBox = {
                        cls: currResponse.regionClass,
                        s3ImageUrl: currResponse.s3ImageUrl,
                        w: currResponse.width,
                        h: currResponse.height,
                        id: currResponse.id,
                        tags: currResponse.tags,
                        equationStr: currResponse.equationStr
                    }

                    imageRegions.push(boundingBox)
                }
                this.setState({ regions: imageRegions, shouldDisplay: true })
            })
            .catch(err => {
                console.error(err)
            })
    }

    fetchNextAndPreviousRequestInfo() {
        if (!this.state.nextRequestId) {
            const url = formatUrl(this.apiUrl + "/requests/next", {
                t: this.state.storedRequest.gcpRequestStartTimeMs
            })
            fetch(url, {
                mode: "cors",
                headers: {
                    "Authorization": `Bearer ${this.authToken}`
                }
            })
                .then(results => {
                    return results.json()
                }).then(response => {
                    if (response.status !== "error") {
                        this.setState({ nextRequestId: response.id, hasNextRequest: true })
                    }
                }).catch(err => {
                    console.error(err);
                })
        }
        if (!this.state.previousRequestId) {
            const url = formatUrl(this.apiUrl + "/requests/previous", {
                t: this.state.storedRequest.gcpRequestStartTimeMs
            })
            fetch(url, {
                mode: "cors",
                headers: {
                    "Authorization": `Bearer ${this.authToken}`
                }
            })
                .then(results => {
                    return results.json()
                }).then(response => {
                    if (response.status !== "error") {
                        this.setState({ previousRequestId: response.id, hasPreviousRequest: true })
                    }
                }).catch(err => {
                    console.error(err);
                })
        }
    }

    handleValueEditModalSubmit() {
        if (this.state.editActionTargetRegion === "" || this.state.modalTextInputValue === "") { return }
        let url = formatUrl(this.apiUrl + "/regions/updateValue", {
            rid: this.state.editActionTargetRegion,
            vid: "equStr",
            v: encodeURIComponent(this.state.modalTextInputValue)
        })
        fetch(url, {
            headers: {
                "Authorization": `Bearer ${this.authToken}`
            }
        })
            .then(results => {
                return results.json()
            }).then(response => {

                this.setState({ shouldDisplayEditValueModal: false, editTargetValueId: "" })

                if (this.state.editTargetValueId === "userInputtedChemicalEquationString") {
                    this.updateUserEquationString(this.state.modalTextInputValue)
                } else if (this.state.editTargetValueId === "verifiedChemicalEquationString") {
                    this.updateVerifiedEquationString(this.state.modalTextInputValue)
                }
                window.location.reload(); // Yes i know very nice
            }).catch(err => {
                console.error(err);
            })
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
        const url = formatUrl(this.apiUrl + "/requests/delete", {
            rid: this.state.storedRequest.id
        })
        fetch(url, {
            mode: "cors",
            headers: {
                "Authorization": `Bearer ${this.authToken}`
            }
        })
            .then(results => {
                return results.json()
            })
            .then(response => {
                if (response.status && response.status === "success") {
                    this.props.history.push({ pathname: "/requests/list" })
                    window.location.reload();
                } else if ((response.status && response.status !== "success") || response.status === 500) {
                    alert("Error: " + (response.description ?? response.error));
                    return
                }
            })
            .catch(err => {
                console.error(err);
            })
    }

    downloadExportedRegions() {
        console.log("export")
    }

    showImageLabelingView() {
        const path = formatUrl("/requests/label", {
            rid: this.state.storedRequest.id,
            imgUrl: encodeURI(this.state.storedRequest.s3ImageUrl)
        })
        this.props.history.push({
            pathname: path,
        })
        window.location.reload();
    }

    processRegions() {
        this.setState({ processingRegions: true })
        const url = formatUrl(this.apiUrl + "/imageProcessor/extract/regions", {
            rid: this.state.storedRequest.id
        })
        fetch(url, {
            headers: {
                "Authorization": `Bearer ${this.authToken}`
            }
        })
            .then(results => {
                return results.json()
            })
            .then(responses => {
                let response = responses[this.state.storedRequest.id]
                for (let i in response) {
                    let currRegionExtractionresponse = response[i]

                    if ((currRegionExtractionresponse.status && currRegionExtractionresponse.status !== "success") || currRegionExtractionresponse.status === 500) {
                        alert("Error: " + (currRegionExtractionresponse.description ?? currRegionExtractionresponse.error));
                        return
                    }
                }

                this.setState({ processingRegions: false })
            })
            .catch(err => {
                console.error(err);
            })
    }

    rotateRegion(regionId, radians) {
        // Rotated image may not display after this call because the browser might use a cached version of the image
        const url = formatUrl(this.apiUrl + "/imageProcessor/transform/rotate/", {
            rid: this.state.storedRequest.id + "_" + regionId,
            r: radians
        })
        fetch(url, {
            headers: {
                "Authorization": `Bearer ${this.authToken}`
            }
        })
            .then(results => {
                return results.json()
            })
            .then(response => {
                if ((response.status && response.status !== "success") || response.status === 500) {
                    alert("Error: " + (response.description ?? response.error));
                    return
                }
                window.location.reload();
            })
            .catch(err => {
                console.error(err);
            })
    }

    render() {
        return !this.state.shouldDisplay ? <LoadingView loadingText="Loading Request..." /> : this.displayView();
    }

    displayRegions() {
        let regionDisplay = []

        for (let i in this.state.regions) {
            const currRegion = this.state.regions[i]

            regionDisplay.push(
                <Stack.Item>
                    <Image source={currRegion.s3ImageUrl} />
                    <Card
                        title="Region Info"
                    >
                        <Card.Section title="Region ID">
                            {currRegion.id}
                        </Card.Section>
                        <Card.Section title="Equation String" actions={[{ icon: EditMajorMonotone, onAction: () => this.setState({ shouldDisplayEditValueModal: true, editActionTargetRegion: currRegion.id }) }]}>
                            {currRegion.equationStr === "" ? "Unavailable" : currRegion.equationStr}
                        </Card.Section>
                        <Card.Section title="S3 Image URL">
                            <Link url={currRegion.s3ImageUrl}> Link </Link>
                        </Card.Section>
                        <Card.Section title="Rotate" actions={[{ icon: CircleLeftMajorMonotone, onAction: () => this.rotateRegion(currRegion.id, 1.57079632679) }, { icon: CircleRightMajorMonotone, onAction: () => this.rotateRegion(currRegion.id, -1.57079632679) }]} />
                    </Card>
                </Stack.Item>
            )
        }

        return regionDisplay
    }

    displayView() {
        let imageStyle = {
            flex: 1,
            width: 450,
            height: 700,
            resizeMode: 'contain'
        };

        const request = this.state.storedRequest
        const pageTitle = "Image Review"
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
                            onClose={() => this.setState({ shouldDisplayEditValueModal: false, editActionTargetRegion: "" })}
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
                                    label={"Edit equation string"}
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
                                    <Card.Section title="Request ID">
                                        <p>{request.id}</p>
                                    </Card.Section>
                                    <Card.Section title="GCP Execution Time (ms)">
                                        <p>{request.gcpRequestEndTimeMs - request.gcpRequestStartTimeMs}</p>
                                    </Card.Section>
                                    <Card.Section title="OD Execution Time (ms)">
                                        <p>{request.onDeviceImageProcessEndTime - request.onDeviceImageProcessStartTime}</p>
                                    </Card.Section>
                                    <Card.Section title="Labeling Status">
                                        {requestStatusBadge}
                                    </Card.Section>
                                    <Card.Section title="S3 Image URL">
                                        <Link url={request.s3ImageUrl}> Link </Link>
                                    </Card.Section>
                                    <Card.Section title="Extract Region(s)" actions={[{ disabled: this.state.processingRegions, icon: WandMajorMonotone, onAction: () => this.processRegions() }]}>
                                        {this.state.processingRegions ? <Spinner size="large" color="teal" /> : ""}
                                    </Card.Section>
                                    <Card.Section title="Download Region Data" actions={[{ icon: FolderDownMajorMonotone, onAction: () => this.downloadExportedRegions() }]} />
                                </Card>
                            </Stack.Item>
                        </Stack>
                        <Stack>
                            {this.displayRegions()}
                        </Stack>
                    </Page>
                </AppProvider>
            </React.Fragment>
        );
    }
}

export default withRouter(RequestViewPage)