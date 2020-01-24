import React from 'react'
import { AppProvider, Page, Card, DataTable, Link } from "@shopify/polaris";
import "@shopify/polaris/styles.css";

class HomePage extends React.Component {

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

        fetch("http://localhost:8080/storedRequests/list", { mode: "cors" })
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
        );
    }
}

export default HomePage