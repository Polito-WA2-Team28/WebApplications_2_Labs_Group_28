import { useState } from "react";
import { Button, Form, Modal, Row } from "react-bootstrap";

export default function ExpertsInfoTab(props) {
    
    const [show, setShow] = useState(false);

    return(
        <>
            <CreateExpertModal show={show} handleClose={() => setShow(false)} />
            <Row>
                <Button onClick={() => setShow(true)}>Create a new Expert</Button>
            </Row>
            <ExpertList experts={props.experts} />
        </>
    )
}

function ExpertList(props) {
    
    const experts = props.experts

    return(
        <>
            {(!experts || experts.length === 0) ? <h2>No experts</h2> 
                : experts.map((expert, index) => {
                    return (
                        <Row>
                            <h3>{index}</h3>
                        </Row>
                    )
                }
            )}
        </>
    )
}

function CreateExpertModal(props) {

    return(
        <Modal show={props.show} onHide={props.handleClose}>
            <Modal.Header>
                <Modal.Title>Create a new Expert</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group>
                        <Form.Label>Expert Name</Form.Label>
                        <Form.Control type="text" placeholder="Enter expert name" />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Expert Email</Form.Label>
                        <Form.Control type="text" placeholder="Enter expert email" />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={props.handleClose}>Close</Button>
                <Button variant="primary" onClick={props.handleClose}>Create</Button>
            </Modal.Footer>
            </Modal>
    )
}