import React, { useState } from "react";

const JournalForm = ({ onEntryCreated }) => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const username = localStorage.getItem("username");
        const password = localStorage.getItem("password");

        if (!username || !password) {
            alert("Login credentials not found. Please log in again.");
            return;
        }

        if (!title.trim() || !content.trim()) {
            alert("Title and content cannot be empty.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/journal/post", {
                method: "POST",
                headers: {
                    "Authorization": "Basic " + btoa(`${username}:${password}`),
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ title, content }),
            });

            if (response.ok) {
                const newEntry = await response.json();
                onEntryCreated(newEntry); // Notify parent component
                setTitle("");
                setContent("");
            } else {
                alert("Failed to create entry. Please try again.");
            }
        } catch (error) {
            console.error("Error creating journal entry:", error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="mb-5">
            <h4 className="mb-3">Create New Journal Entry</h4>
            <div className="mb-3">
                <label className="form-label">Title</label>
                <input
                    type="text"
                    className="form-control"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />
            </div>
            <div className="mb-3">
                <label className="form-label">Content</label>
                <textarea
                    className="form-control"
                    rows="4"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                />
            </div>
            <button type="submit" className="btn btn-primary">Add Entry</button>
        </form>
    );
};

export default JournalForm;
