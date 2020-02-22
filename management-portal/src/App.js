import React from "react";
import { Route, BrowserRouter as Router } from 'react-router-dom'
import HomePage from './Components/HomePage'
import StoredRequestListPage from './Components/StoredRequestListPage'
import RequestViewPage from './Components/RequestViewPage'
import ImageLabelingPage from './Components/ImageLabelingPage'

function App() {
    return (
        <Router>
            <div>
                <Route exact path="/" component={HomePage} />
                <Route exact path="/requests/list" component={StoredRequestListPage} />
                <Route exact path="/requests/view" component={RequestViewPage} />
                <Route exact path="/requests/label" component={ImageLabelingPage} />
            </div>
        </Router>
    );
}


export default App;
