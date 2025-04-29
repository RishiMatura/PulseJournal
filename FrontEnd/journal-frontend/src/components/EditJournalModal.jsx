import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const EditJournalModal = ({ show, handleClose, entry, onUpdateSuccess, onDeleteSuccess }) => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    useEffect(() => {
        if (entry) {
            setTitle(entry.title || "");
            setContent(entry.content || "");
        }
    }, [entry]);

    const handleUpdate = async () => {
        const username = localStorage.getItem("username");
        const password = localStorage.getItem("password");
    
        if (!title.trim() || !content.trim()) {
            alert("Title and content cannot be empty.");
            return;
        }
    
        try {
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
                onUpdateSuccess(updatedEntry); // already contains emotion + sentiment now
                handleClose();
            } else {
                alert("Update failed.");
            }
        } catch (error) {
            console.error("Error updating entry:", error);
        }
    };
    
    

    const handleDelete = async () => {
        const confirmDelete = window.confirm("Are you sure you want to delete this entry?");
        if (!confirmDelete) return;
    
        // Optimistic: instantly update UI
        onDeleteSuccess(entry.id); // remove from state
        handleClose(); // close modal
    
        try {
            const username = localStorage.getItem("username");
            const password = localStorage.getItem("password");
    
            const response = await fetch(`http://localhost:8080/journal/id/${entry.id}`, {
                method: "DELETE",
                headers: {
                    Authorization: "Basic " + btoa(`${username}:${password}`),
                    "Content-Type": "application/json",
                },
            });
    
            if (!response.ok) {
                alert("Failed to delete entry. Please refresh.");
                // Optional: re-add the entry to state if needed
            }
        } catch (error) {
            console.error("Error deleting entry:", error);
            alert("An error occurred.");
            // Optional: re-add the entry to state if needed
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

    {/* Emotion breakdown */}
    {entry?.allEmotions && entry.allEmotions.length > 0 && (
        <div className="mt-4">
            <h6>Emotion Breakdown</h6>
            {entry.allEmotions.map((emotion, idx) => (
                <div key={idx} className="mb-2">
                    <small>{emotion.label} ({(emotion.score * 100).toFixed(1)}%)</small>
                    <div className="progress" style={{ height: '6px' }}>
                        <div
                            className={`progress-bar bg-${emotion.label === entry.topEmotion.label ? 'primary' : 'secondary'}`}
                            role="progressbar"
                            style={{ width: `${emotion.score * 100}%` }}
                        />
                    </div>
                </div>
            ))}
        </div>
    )}
</Modal.Body>

            <Modal.Footer className="d-flex justify-content-between">
                <Button variant="danger" onClick={handleDelete}>
                    🗑️ Delete
                </Button>
                <div>
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleUpdate} className="ms-2">
                        Save Changes
                    </Button>
                </div>
            </Modal.Footer>
        </Modal>
    );
};

export default EditJournalModal;
