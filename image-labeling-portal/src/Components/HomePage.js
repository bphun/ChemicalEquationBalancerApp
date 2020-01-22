import React from 'react'
import { AppProvider, Page, Card, DataTable, Link } from "@shopify/polaris";
import "@shopify/polaris/styles.css";
import { Redirect } from 'react-router-dom';

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
history.push("/view?rid="+storedRequest.id)
        // return (<Redirect to={"/view?rid="+storedRequest.id}/>);
        // // this.props.history.push({
        // //     pathname: "/view?rid="+storedRequest.id,
        // // })
    }

    generateDataTable() {
        const columnContentTypes = [
            "text",
            "text",
            "numeric",
            "numeric",
            "numeric"
        ]

        const headings = [
            "id",
            "s3ImageUrl",
            "gcpProcessingTimeMs",
            "onDeviceProcessingTimeMs",
            "verifiedChemicalEquationString"
        ]

        // const [rows, setRows] = useState([]);

        const rows = []

        fetch("http://localhost:8080/storedRequests/list", { mode: "cors" })
            .then(results => {
                return results.json();
            }).then(storedRequests => {
                for (const i in storedRequests) {
                    let storedRequest = storedRequests[i];
                    let row = [];

                    row[0] = <Link onClick={() => this.onChange(storedRequest)}> {storedRequest.id} </Link >;
                    row[1] = <Link url={storedRequest.s3ImageUrl}> Link </Link>;
                    row[2] = storedRequest.gcpRequestEndTimeMs - storedRequest.gcpRequestStartTimeMs;
                    row[3] = storedRequest.onDeviceImageProcessEndTime - storedRequest.onDeviceImageProcessStartTime;
                    row[4] = storedRequest.verifiedChemicalEquationString ? storedRequest.verifiedChemicalEquationString : "Unavailable";

                    rows.push(row);
                    // setRows(rows => [...rows, row])
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