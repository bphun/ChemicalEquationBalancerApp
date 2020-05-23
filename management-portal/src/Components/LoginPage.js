import React from 'react'
import "../App.css"
import { AppProvider, TextField, FormLayout, Page, Form, Button, Caption } from "@shopify/polaris";
import Auth from '../Utility/Auth';
require('dotenv').config(process.env.NODE_ENV === "development" ? "../../.env.development" : "../../.env.production")

class LoginPage extends React.Component {

    constructor(props) {
        super(props)

        if (Auth.isLoggedIn()) {
            this.props.history.push({
                pathname: "/home"
            })
            window.location.reload();
        }

        this.state = {
            usernameText: "",
            passwordText: "",
            didSubmitForm: false,
            authStatusCaptionText: ""
        }

        this.apiHostname = process.env.REACT_APP_AUTH_API_HOSTNAME
    }

    clearAuthStatusCaptionAfterDelay(delay) {
        setTimeout(() => {
            this.setState({ authStatusCaptionText: "" })
        }, delay)
    }

    handleSubmit() {
        if (this.state.usernameText === "" || this.state.passwordText === "") {
            this.setState({ authStatusCaptionText: "All fields must be filled" })
            this.clearAuthStatusCaptionAfterDelay(10000)
            return;
        }

        const requestConfig = {
            method: "POST",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Accept": 'application/json',
            },
            body: JSON.stringify({
                "username": this.state.usernameText,
                "password": this.state.passwordText
            })
        }

        fetch(this.apiHostname + "/auth/login", requestConfig)
            .then(results => {
                return results.json()
            })
            .then(response => {
                if (response["status"] === "success") {
                    Auth.writeAuthCookie(response.token)
                    this.props.history.push({
                        pathname: "/home"
                    })
                    window.location.reload();
                } else {
                    this.setState({ authStatusCaptionText: response.description })
                    this.clearAuthStatusCaptionAfterDelay(10000)
                }
            })
            .catch(err => {
                console.error(err)
            })
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
                    <Page title="Log in" class="login-page" fullHeight={true}>
                        <Form onSubmit={() => this.handleSubmit()}>
                            <FormLayout>
                                <TextField label="Username" value={this.state.usernameText} onChange={(value) => { this.setState({ usernameText: value }) }} />
                                <TextField label="Password" type="password" value={this.state.passwordText} onChange={(value) => { this.setState({ passwordText: value }) }} />
                                <Button primary loading={this.state.didSubmitForm} onClick={() => this.handleSubmit()}>Log in</Button>
                                <Caption>{this.state.authStatusCaptionText}</Caption>
                            </FormLayout>
                        </Form>
                    </Page>
                </AppProvider>
            </React.Fragment>
        );
    }
}

export default LoginPage;