import React from 'react'
import { AppProvider, Page, Card, Spinner } from "@shopify/polaris";

class LoadingView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loadingText: this.props.loadingText ?? "Loading..."
        }
    }

    render() {
        return (
            <React.Fragment>
                <head>
                    <title>Image Labeling Portal</title>
                    <meta charSet="utf-8" />
                    <link rel="stylesheet" href="https://unpkg.com/@shopify/polaris@4.11.0/styles.css" />
                </head>
                <AppProvider>
                    <Page title={this.state.loadingText} >
                        <Card>
                            <Spinner accessibilityLabel={this.state.loadingText} hasFocusableParent={false} />
                        </Card>
                    </Page>
                </AppProvider>
            </React.Fragment>
        );
    }
}

export default LoadingView;