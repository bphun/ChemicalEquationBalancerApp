import React from "react";
import { Switch, Redirect, Route, BrowserRouter as Router } from 'react-router-dom'
import HomePage from './Components/HomePage'
import StoredRequestListPage from './Components/StoredRequestListPage'
import RequestViewPage from './Components/RequestViewPage'
import ImageLabelingPage from './Components/ImageLabelingPage'
import LoginPage from "./Components/LoginPage";

function App() {
    return (
        <Router>
            <Switch>
                <Route exact path="/home" component={HomePage} />
                <Route exact path="/login" component={LoginPage} />
                <Route exact path="/requests/list" component={StoredRequestListPage} />
                <Route exact path="/requests/view" component={RequestViewPage} />
                <Route exact path="/requests/label" component={ImageLabelingPage} />
                <Route render={() => <Redirect to={{ pathname: "/home" }} />} />
            </Switch>
        </Router>
    );
}


export default App;
