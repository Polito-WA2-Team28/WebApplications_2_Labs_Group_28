import React from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import PropTypes from 'prop-types';

function SaveChangesModal(props) {
const { show, user, handleClose, handleSave } = props;

return (
    <Modal show={show} onHide={handleClose}>
    <Modal.Header closeButton>
        <Modal.Title>Save changes?</Modal.Title>
    </Modal.Header>

    <Modal.Body>
        <p>Are you sure you want to save the changes?</p>
    </Modal.Body>

    <Modal.Footer>
        <Button variant="primary" onClick={handleClose}>
        Cancel
        </Button>
        <Button variant="success" onClick={() => handleSave(user)}>
        Save
        </Button>
    </Modal.Footer>
    </Modal>
);
}

SaveChangesModal.propTypes = {
    user: PropTypes.object.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleSave: PropTypes.func.isRequired,
};
export default SaveChangesModal;
