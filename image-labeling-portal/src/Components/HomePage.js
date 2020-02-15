import React from 'react'
import { AppProvider, Page, Card, DataTable, Link, Badge } from "@shopify/polaris";
// import "@shopify/polaris/styles.css";
// import Head from "next/head"
require('dotenv').config(process.env.NODE_ENV === "development" ? "../../.env.development" : "../../.env.production")

class HomePage extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            storedRequestInfo: [],
            shouldDisplay: false
        }

        this.apihostname = process.env.REACT_APP_API_HOSTNAME
    }

    componentDidMount() {
        this.getStoredRequests()
    }

    render() {
        return !this.state.shouldDisplay ? <div></div> : (
            <div>
                {this.generateDataTable()}
            </div>
        );
    }

    onChange = (storedRequest) => {
        const { history } = this.props;
        history.push("/view?rid=" + storedRequest.id)
    }

    getStoredRequests() {
        const requests = []
        fetch(this.apihostname + "/storedRequests/list", { mode: "cors" })
            .then(results => {
                return results.json();
            }).then(storedRequests => {
                for (const i in storedRequests) {
                    const currRequest = storedRequests[i];
                    const requestDate = new Date(currRequest.gcpRequestStartTimeMs)
                    let storedRequestInfo = [];
                    var requestStatusBadge;

                    if (currRequest.labelingStatus === "INCOMPLETE") {
                        requestStatusBadge = <Badge status="attention" progress="incomplete">Incomplete</Badge>
                    } else if (currRequest.labelingStatus === "IN_PROGRESS") {
                        requestStatusBadge = <Badge status="info" progress="partiallyComplete">In Progress</Badge>
                    } else if (currRequest.labelingStatus === "LABELED") {
                        requestStatusBadge = <Badge status="success" progress="complete">Labeled</Badge>
                    } else {
                        requestStatusBadge = <Badge status="warning">Unavailable</Badge>
                    }

                    storedRequestInfo[0] = <Link onClick={() => this.onChange(currRequest)}> {currRequest.id} </Link >;
                    storedRequestInfo[1] = <Link url={currRequest.s3ImageUrl}> Link </Link>;
                    storedRequestInfo[2] = requestStatusBadge
                    storedRequestInfo[3] = requestDate.toLocaleDateString("en-US") + " " + requestDate.toLocaleTimeString("en-US")
                    storedRequestInfo[4] = currRequest.gcpRequestEndTimeMs - currRequest.gcpRequestStartTimeMs;
                    storedRequestInfo[5] = currRequest.onDeviceImageProcessEndTime - currRequest.onDeviceImageProcessStartTime;
                    storedRequestInfo[6] = currRequest.verifiedChemicalEquationString ? currRequest.verifiedChemicalEquationString : "Unavailable";

                    requests.push(storedRequestInfo);
                }

                this.setState({ storedRequestInfo: requests, shouldDisplay: true })
            }).catch(error => {
                return (
                    <h1>Error</h1>
                )
            });
    }

    generateDataTable() {
        const columnContentTypes = [
            "text",
            "text",
            "text",
            "text",
            "numeric",
            "numeric",
            "numeric"
        ]

        const headings = [
            "id",
            "s3ImageUrl",
            "Labeling Status",
            "Request Date",
            "gcpProcessingTimeMs",
            "onDeviceProcessingTimeMs",
            "verifiedChemicalEquationString"
        ]

        return (
            <React.Fragment>
                <head>
                    <title>Image Labeling Portal</title>
                    <meta charSet="utf-8" />
                    <link rel="stylesheet" href="https://unpkg.com/@shopify/polaris@4.11.0/styles.css" />
                </head>
                <AppProvider>
                    <Page title="Stored Requests" >
                        <Card>
                            <DataTable
                                columnContentTypes={columnContentTypes}
                                headings={headings}
                                rows={this.state.storedRequestInfo}
                            />
                        </Card>
                    </Page>
                </AppProvider>
            </React.Fragment>
        );
    }
}

export default HomePage