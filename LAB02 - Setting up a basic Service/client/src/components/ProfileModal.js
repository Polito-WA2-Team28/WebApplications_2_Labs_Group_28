import { Modal, Table } from "react-bootstrap";

export function ProfileModal(props) {
    //return a modal
    return <Modal fullscreen show={props.show} onHide={props.handleClose}>
        <Modal.Header closeButton>
            <Modal.Title>Profile</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Table>
                <thead>
                    <tr>
                    <th>name</th>
                    <th>surname</th>
                    <th>registrationDate</th>
                    <th>birthDate</th>
                    <th>email</th>
                    <th>phoneNumber</th>
                    </tr>
                </thead>
                <tr>
                    <td>{props.profile.name}</td>
                    <td>{props.profile.surname}</td>
                    <td>{props.profile.registrationDate}</td>
                    <td>{props.profile.birthDate}</td>
                    <td>{props.profile.email}</td>
                    <td>{props.profile.phoneNumber}</td>
                </tr>
            
                </Table>
        </Modal.Body>
    </Modal>
}