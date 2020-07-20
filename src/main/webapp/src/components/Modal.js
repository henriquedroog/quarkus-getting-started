import React from 'react';
import { Modal,Button } from 'react-bootstrap';
export default ({
                  title,
                  onClose,
                  modalBody,
                  modalFooter,
                  show,
                }) => {
  return (
    <>
      <Modal
        show={show}
        onHide={onClose}
        backdrop="static"
        keyboard={false}
        size="lg"
        aria-labelledby="contained-modal-title-vcenter"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>{title}</Modal.Title>
          <Modal.Body>
            {modalBody && modalBody()}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => onClose(false)}>
              Close
            </Button>
            {modalFooter && modalFooter()}
          </Modal.Footer>
        </Modal.Header>
      </Modal>
    </>
  )
};