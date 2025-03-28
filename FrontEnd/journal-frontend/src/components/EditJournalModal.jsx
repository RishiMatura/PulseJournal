import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const EditJournalModal = ({ show, handleClose, entry, onUpdateSuccess }) => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    useEffect(() => {
        if (entry) {
            setTitle(entry.title || "");
            setContent(entry.content || "");
        }
    }, [entry]);

    const handleUpdate = async () => {
        console.log("Updating...");
        const username = localStorage.getItem("username");
        const password = localStorage.getItem("password");
    
        if (!title.trim() || !content.trim()) {
            alert("Title and content cannot be empty.");
            return;
        }
    
        try {
            console.log("ENTRY OBJECT IN MODAL:", entry);

            const response = await fetch(`http://localhost:8080/journal/id/${entry.id}`, {
                method: "PUT",
                headers: {
                  "Authorization": "Basic " + btoa(`${username}:${password}`),
                  "Content-Type": "application/json"
                },
                body: JSON.stringify({ title, content }),
              });
              
    
            if (response.ok) {
                const updatedEntry = await response.json();
                onUpdateSuccess(updatedEntry);
                handleClose();
            } else {
                alert("Update failed.");
            }
        } catch (error) {
            console.error("Error updating entry:", error);
        }
    };
    

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Edit Journal Entry</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Content</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={4}
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleUpdate}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default EditJournalModal;
