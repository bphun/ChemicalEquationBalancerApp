import React from 'react'
import { AppProvider, Page, Card, DataTable, Link } from "@shopify/polaris";
// import "@shopify/polaris/styles.css";
// import Head from "next/head"
require('dotenv').config(process.env.NODE_ENV === "development" ? "../../.env.development" : "../../.env.production")

class HomePage extends React.Component {

    constructor(props) {
        super(props)

        this.apihostname = process.env.REACT_APP_API_HOSTNAME
    }

    render() {
        return (
            <div>
                {this.generateDataTable()}
            </div>
        );
    }

    onChange = (storedRequest) => {
        const { history } = this.props;
        history.push("/view?rid=" + storedRequest.id)
    }

    generateDataTable() {
        const columnContentTypes = [
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
            "Request Date",
            "gcpProcessingTimeMs",
            "onDeviceProcessingTimeMs",
            "verifiedChemicalEquationString"
        ]

        const rows = []
        
        fetch(this.apihostname + "/storedRequests/list", { mode: "cors" })
            .then(results => {
                return results.json();
            }).then(storedRequests => {
                for (const i in storedRequests) {
                    const storedRequest = storedRequests[i];
                    const requestDate = new Date(storedRequest.gcpRequestStartTimeMs)
                    let row = [];

                    row[0] = <Link onClick={() => this.onChange(storedRequest)}> {storedRequest.id} </Link >;
                    row[1] = <Link url={storedRequest.s3ImageUrl}> Link </Link>;
                    row[2] = requestDate.toLocaleDateString("en-US") + " " + requestDate.toLocaleTimeString("en-US")
                    row[3] = storedRequest.gcpRequestEndTimeMs - storedRequest.gcpRequestStartTimeMs;
                    row[4] = storedRequest.onDeviceImageProcessEndTime - storedRequest.onDeviceImageProcessStartTime;
                    row[5] = storedRequest.verifiedChemicalEquationString ? storedRequest.verifiedChemicalEquationString : "Unavailable";

                    rows.push(row);
                }
            }).catch(error => {
                return (
                    <h1>Error</h1>
                )
            });

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
                            rows={rows}
                        />
                    </Card>
                </Page>
                </AppProvider>
            </React.Fragment>
        );
    }
}

export default HomePage