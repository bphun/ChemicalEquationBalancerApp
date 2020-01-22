import React from "react";
import { Route, BrowserRouter as Router } from 'react-router-dom'
import HomePage from './Components/HomePage'
import RequestViewPage from './Components/RequestViewPage'

function App() {
    return (
        <Router>
            <div>
                <Route exact path="/" component={HomePage} />
                <Route exact path="/view" component={RequestViewPage} />
            </div>
        </Router>
    );
}


export default App;
