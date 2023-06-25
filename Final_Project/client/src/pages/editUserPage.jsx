import React, { useState } from 'react'
import { Container, Row, Col, Button, Form } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {
  faUser,
  faEnvelope,
  faCalendar,
  faPhone,
  faSave,
  faTimes,
} from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import { successToast, errorToast } from '../components/toastHandler'
import SaveChangesModal from '../components/SaveChangesModal'
import validator from 'validator'
import dayjs from 'dayjs'; // Import dayjs

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD'); // Format the date using dayjs
};

export default function EditUserPage(props) {
      const { user } = props;
    const [name, setName] = useState(user.name);
    const [surname, setSurname] = useState(user.surname);
    const [username, setUsername] = useState(user.username);
    const [birthDate, setBirthDate] = useState(formatDate(user.birthDate));
    const [email, setEmail] = useState(user.email);
    const [phoneNumber, setPhoneNumber] = useState(user.phoneNumber);
    const [showModal, setShowModal] = useState(false);
    const [newUser  , setNewUser] = useState(user);

    const navigate = useNavigate();
    
    const openModal = () => {
        setShowModal(true);
    };
    
    const closeModal = () => {
        setShowModal(false);
    };

    const handleCancelClick = () => {
        navigate('/user');
    };


    const handleSaveClick = (event) => {
        event.preventDefault();

        const errors = {};

        // Validate name
        if (validator.isEmpty(name)) {
            errors.name = 'Name is required';
        } else if (!validator.isLength(name, { max: 30 })) {
            errors.name = 'Name should not exceed 30 characters';
        }

        // Validate surname
        if (validator.isEmpty(surname)) {
            errors.surname = 'Surname is required';
        } else if (!validator.isLength(surname, { max: 30 })) {
            errors.surname = 'Surname should not exceed 30 characters';
        }

        // Validate phone number
        if (validator.isEmpty(phoneNumber)) {
            errors.phoneNumber = 'Phone number is required';
        } else if (!validator.matches(phoneNumber, /^[-+0-9 ]{7,15}$/)) {
            errors.phoneNumber = 'Invalid phone number format';
        }

        if (Object.keys(errors).length === 0) {
            var tempUser = user;
            tempUser.name = name;
            tempUser.surname = surname;
            // tempUser.username = username;
            //tempUser.email = email;
            tempUser.birthDate = formatDate(birthDate);
            tempUser.phoneNumber = phoneNumber;
            setNewUser(tempUser)
            openModal()
        } else {
            console.log(errors);
            Object.values(errors).forEach((errorMessage) => {
            errorToast(errorMessage);
            });
        }
    };

    const handleSaveConfirmation = (user) => {
        // Implement logic to save the data
        setShowModal(false);
        props.handleEdit(user);
        navigate('/user');
    };

    const handleBirthDateChange = (date) => {
        const selectedDate = formatDate(date);
        setBirthDate(selectedDate);
    };      

    return (
        <Container>
            <Row className="justify-content-center">
                <Col lg={8} md={10} className="py-4">
                <h1>Edit User Profile</h1>
                <Row>
                    <Col xs={12} md={6}>
                    <Form>
                        <Form.Group className="m-2">
                        <div>
                            <FontAwesomeIcon icon={faUser} className="profile-icon" />
                            <Form.Label><strong>Name:</strong></Form.Label>
                        </div>
                        <Form.Control
                            type="text"
                            value={name}
                            onChange={(event) => setName(event.target.value)}
                        />
                        </Form.Group>
                        <Form.Group className="m-2">
                        <div>
                            <FontAwesomeIcon icon={faUser} className="profile-icon" />
                            <Form.Label><strong>Surname:</strong></Form.Label>
                        </div>
                        <Form.Control
                            type="text"
                            value={surname}
                            onChange={(event) => setSurname(event.target.value)}
                        />
                        </Form.Group>
                        <Form.Group className="m-2">
                        <div>
                            <FontAwesomeIcon icon={faUser} className="profile-icon" />
                            <Form.Label><strong>Username:</strong></Form.Label>
                        </div>
                        <Form.Control
                            type="text"
                            value={username}
                            onChange={(event) => setUsername(event.target.value)}
                            disabled
                        />
                        </Form.Group>
                        <Form.Group className="m-2">
                            <div>
                                <FontAwesomeIcon icon={faCalendar} className="profile-icon" />
                                <Form.Label><strong>Registration Date:</strong></Form.Label>
                            </div>
                            <Form.Control type="text" defaultValue={formatDate(user.registrationDate)} disabled />
                        </Form.Group>
                    </Form>
                    </Col>
                    <Col xs={12} md={6}>
                    <Form>
                        <Form.Group className="m-2">
                            <div>
                                <FontAwesomeIcon icon={faCalendar} className="profile-icon" />
                                <Form.Label><strong>Birth Date:</strong></Form.Label>
                            </div>
                            <DatePicker
                                className="form-control"
                                name="birthDate"
                                selected={dayjs(birthDate).toDate()}
                                dateFormat="yyyy-MM-dd"
                                onChange={handleBirthDateChange}
                            />
                        </Form.Group>
                        <Form.Group className="m-2">
                            <div>
                                <FontAwesomeIcon icon={faEnvelope} className="profile-icon" />
                                <Form.Label><strong>Email:</strong></Form.Label>
                            </div>
                        <Form.Control
                            type="email"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                            disabled
                        />
                        </Form.Group>
                        <Form.Group className="m-2">
                        <div>
                            <FontAwesomeIcon icon={faPhone} className="profile-icon" />
                            <Form.Label><strong>Phone Number:</strong></Form.Label>
                        </div>
                        <Form.Control
                            type="text"
                            value={phoneNumber}
                            onChange={(event) => setPhoneNumber(event.target.value)}
                        />
                        </Form.Group>
                    </Form>
                    </Col>
                </Row>
                <Row className="justify-content-center mt-4">
                    <Col xs={12} md="auto">
                    <Button variant="primary" onClick={handleCancelClick}>
                        Cancel
                    </Button>
                    </Col>
                    <Col xs={12} md="auto">
                    <Button variant="success" className="ml-md-2" onClick={handleSaveClick}>
                        Save Changes
                    </Button>
                    </Col>
                </Row>
                </Col>
            </Row>
            <SaveChangesModal
                show={showModal}
                handleClose={closeModal}
                handleSave={handleSaveConfirmation}
                user={newUser}
            />
        </Container>
    );


}
