import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faEnvelope, faCalendar, faPhone, faEdit } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs'; // Import dayjs

export default function UserPage(props) {
  const { user } = props;
  const navigate = useNavigate();

  const handleEditProfileClick = () => {
    navigate('/editUser');
  };

  const formatDate = (date) => {
    return dayjs(date).format('YYYY-MM-DD'); // Format the date using dayjs
  };

  return (
    <Container>
      <Row className="justify-content-center">
        <Col lg={8} md={10} className="py-4">
          <h1>User Profile</h1>
          <Row>
            <Col xs={12} md={6}>
              <div className="m-2">
                <div>
                  <FontAwesomeIcon icon={faUser} className="profile-icon" />
                  <label><strong>Name:</strong></label>
                </div>
                <span>{user.name}</span>
              </div>
              <div className="m-2">
                <div>
                  <FontAwesomeIcon icon={faUser} className="profile-icon" />
                  <label><strong>Surname:</strong></label>
                </div>
                <span>{user.surname}</span>
              </div>
              <div className="m-2">
                <div>
                  <FontAwesomeIcon icon={faUser} className="profile-icon" />
                  <label><strong>Username:</strong></label>
                </div>
                <span>{user.username}</span>
              </div>
              <div className="m-2">
                <div>
                  <FontAwesomeIcon icon={faCalendar} className="profile-icon" />
                  <label><strong>Registration Date:</strong></label>
                </div>
                <span>{formatDate(user.registrationDate)}</span>
              </div>
            </Col>
            <Col xs={12} md={6}>
              <div className="m-2">
                <div>
                  <FontAwesomeIcon icon={faCalendar} className="profile-icon" />
                  <label><strong>Birth Date:</strong></label>
                </div>
                <span>{user.birthDate}</span>
              </div>
              <div className="m-2">
                <div>
                  <FontAwesomeIcon icon={faEnvelope} className="profile-icon" />
                  <label><strong>Email:</strong></label>
                </div>
                <span>{user.email}</span>
              </div>
              <div className="m-2">
                <div>
                  <FontAwesomeIcon icon={faPhone} className="profile-icon" />
                  <label><strong>Phone Number:</strong></label>
                </div>
                <span>{user.phoneNumber}</span>
              </div>
            </Col>
          </Row>
          <Row className="justify-content-center mt-4">
            <Col xs={12} md="auto">
              <Button variant="primary" onClick={handleEditProfileClick}>
                <FontAwesomeIcon icon={faEdit} className="mr-2" />
                Edit Profile
              </Button>
            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}
