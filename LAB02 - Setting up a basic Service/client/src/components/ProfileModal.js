import dayjs from "dayjs";
import { Modal, Table } from "react-bootstrap";

export function ProfileModal(props) {

    const registrationDate = dayjs(props.profile.registrationDate).format('YYYY-MM-DD');
    const birthDate = dayjs(props.profile.birthDate).format('YYYY-MM-DD');

    //return a modal
    return <Modal size="xl" show={props.show} onHide={props.handleClose}>
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
                <tbody>
                <tr>
                    <td>{props.profile.name}</td>
                    <td>{props.profile.surname}</td>
                    <td>{registrationDate}</td>
                    <td>{birthDate}</td>
                    <td>{props.profile.email}</td>
                    <td>{props.profile.phoneNumber}</td>
                    </tr>
                </tbody>
            
                </Table>
        </Modal.Body>
    </Modal>
}