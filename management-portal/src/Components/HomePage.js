import React from 'react'
import { AppProvider, Page, Navigation } from "@shopify/polaris";
import { AnalyticsMajorMonotone, HomeMajorTwotone, ImagesMajorMonotone } from '@shopify/polaris-icons';

class HomePage extends React.Component {

    render() {
        return (
            <React.Fragment>
                <head>
                    <title>Image Labeling Portal</title>
                    <meta charSet="utf-8" />
                    <link rel="stylesheet" href="https://unpkg.com/@shopify/polaris@4.11.0/styles.css" />
                </head>
                <AppProvider>
                    <Page title="Management Portal">
                        <Navigation location="/">
                            <Navigation.Section
                                items={[
                                    {
                                        url: "/",
                                        label: "Home",
                                        icon: HomeMajorTwotone
                                    },
                                    {
                                        url: "/requests/list",
                                        label: "Image Labeling Tool",
                                        icon: ImagesMajorMonotone                                  
                                    },
                                    {
                                        url: "/metrics/",
                                        label: "Grafana Metrics Dashboard",
                                        icon: AnalyticsMajorMonotone
                                    }
                                ]}
                            />
                        </Navigation>
                    </Page>
                </AppProvider>
            </React.Fragment>
        );
    }

}

export default HomePage
