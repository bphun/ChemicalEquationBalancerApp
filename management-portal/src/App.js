import React, { Component } from "react";
import { Switch, Redirect, Route, BrowserRouter as Router } from 'react-router-dom'
import HomePage from './Components/HomePage'
import StoredRequestListPage from './Components/StoredRequestListPage'
import RequestViewPage from './Components/RequestViewPage'
import ImageLabelingPage from './Components/ImageLabelingPage'
import LoginPage from "./Components/LoginPage";
import Auth from "./Utility/Auth"

export default class App extends Component {
    render() {
        const PrivateRoute = ({ component: Component, ...rest }) => (
            <Route {...rest} render={(props) => (
                Auth.isLoggedIn() === true
                    ? <Component {...props} />
                    : <Redirect to={{
                        pathname: '/login',
                        state: { from: props.location }
                    }} />
            )} />
        )
                
        return (
            <Router>
                <Switch>
                    <Route exact path="/login" component={LoginPage} />
                    <PrivateRoute exact path="/home" component={HomePage} />
                    <PrivateRoute exact path="/requests/list" component={StoredRequestListPage} />
                    <PrivateRoute exact path="/requests/view" component={RequestViewPage} />
                    <PrivateRoute exact path="/requests/label" component={ImageLabelingPage} />
                    <Route render={() => <Redirect to={{ pathname: "/home" }} />} />
                </Switch>
            </Router>
        );
    }
}
