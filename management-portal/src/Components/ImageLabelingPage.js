import React from 'react'
import ReactImageAnnotate from "react-image-annotate"
import queryString from 'query-string';
import "../App.css"
require('dotenv').config(process.env.NODE_ENV === "development" ? "../../.env.development" : "../../.env.production")

class ImageLabelingPage extends React.Component {

    constructor(props) {
        super(props)

        this.state = {
            imageUrl: "",
            requestId: "",
            boundingBoxes: [],
            ready: false
        }

        this.apiHostname = process.env.REACT_APP_API_HOSTNAME
    }

    componentDidMount() {
        let parsedQueryString = queryString.parse(this.props.location.search)
        const requestId = parsedQueryString.rid
        const imageUrl = decodeURI(parsedQueryString.imgUrl)
        this.setState({ requestId: requestId, imageUrl: imageUrl })

        this.getBoundingBoxes(requestId)
        this.setInProgressLabelingStatus(requestId);
    }

    getBoundingBoxes(requestId) {
        let imageBoundingsBoxes = []
        fetch(this.apiHostname + "/storedRequests/getBoundingBoxes?rid=" + requestId, { mode: "cors" })
            .then(results => {
                return results.json()
            })
            .then(fetchedBoundingBoxes => {
                for (let i in fetchedBoundingBoxes) {
                    let currResponse = fetchedBoundingBoxes[i]
                    const randColor = Math.random() * 360
                    let boundingBox = {
                        cls: currResponse.regionClass,
                        color: `hsl(${randColor},100%,50%)`,
                        w: currResponse.width / 450,
                        x: currResponse.originX / 450,
                        h: currResponse.height / 700,
                        y: currResponse.originY / 700,
                        id: currResponse.id,
                        tags: currResponse.tags,
                        type: "box",
                        editingLabels: false,
                        highlighted: false,
                    }

                    imageBoundingsBoxes.push(boundingBox)
                }
                this.setState({ boundingBoxes: imageBoundingsBoxes, ready: true })
            })
            .catch(err => {
                console.err(err)
            })
    }

    setInProgressLabelingStatus(requestId) {
        let url = this.apiHostname + "/storedRequests/updateValue"
        url += "?rid=" + requestId
        url += "&vid=labelingStatus"
        url += "&v=IN_PROGRESS"

        fetch(url, { mode: "cors" })
            .catch(err => {
                console.err(err)
            })
    }

    redirect(path) {
        const { history } = this.props;
        history.push(path)
    }

    comparer(otherArray) {
        return function (current) {
            return otherArray.filter(function (other) {
                return other.id === current.id
            }).length === 0;
        }
    }

    onExitClick(images) {
        let boundingBoxes = []
        for (let i in images[0].regions) {
            const region = images[0].regions[i]
            
            const boundingBox = {
                id: region.id,
                requestInfoId: this.state.requestId,
                regionClass: region.cls,
                width: region.w * 450,
                originX: region.x * 450,
                height: region.h * 700,
                originY: region.y * 700,
                tags: region.tags ? region.tags : []
            }

            boundingBoxes.push(boundingBox)
        }

        if (boundingBoxes.length <= 0) {
            this.redirect("/requests/view?rid=" + this.state.requestId)
        }

        const boundingBoxDiff = {
            requestId: this.state.requestId,
            modified: boundingBoxes,
            deleted: this.state.boundingBoxes.filter(this.comparer(boundingBoxes))
        }
        const config = {
            method: "POST",
            mode: "cors",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(boundingBoxDiff)
        }

        fetch(this.apiHostname + "/storedRequests/updateBoundingBoxes", config)
            .then(results => {
                return results.json()
            })
            .then(response => {
                if (response["status"] === "success") {
                    this.redirect("/requests/view?rid=" + this.state.requestId)
                } else {
                    alert("Error: " + response["description"])
                }
            })
            .catch(err => {
            })
    }

    render() {

        return !this.state.ready ? <div></div> : (
            <ReactImageAnnotate
                selectedImage={this.state.imageUrl}
                taskDescription="# Draw region around each chemical equation"
                images={[{ src: this.state.imageUrl, name: this.state.requestId, regions: this.state.boundingBoxes }]}
                regionTagList={["HAND_WRITTEN", "COMPUTER_SCREEN", "DIR_BLUR", "LOW_RES", "OBFUSCATED", "LOW_LIGHT", "HIGH_LIGHT", "OFF_FOCUS", "HAS_COEFFICIENTS"]}
                regionClsList={["CHEM_EQUATION_NO_EQU", "CHEM_EQUATION_EQU"]}
                pointDistancePrecision={2}
                enabledTools={["select", "create-box",]}
                onExit={(state) => this.onExitClick(state.images.map((image) => ({ name: image.name, regions: image.regions })))}
            />
        );
    }
}

export default ImageLabelingPage