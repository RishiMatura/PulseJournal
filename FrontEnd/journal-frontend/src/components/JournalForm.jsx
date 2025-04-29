import React, { useState, useEffect, useRef } from "react";

const JournalForm = ({ onEntryCreated }) => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [isListening, setIsListening] = useState(false);
    const [error, setError] = useState(null);
    
    const recognitionRef = useRef(null);
    const lastTranscriptRef = useRef(""); // Store last transcript to avoid repetition
    const manuallyStoppedRef = useRef(false); // Track manual stop

    useEffect(() => {
        if (!("webkitSpeechRecognition" in window)) {
            alert("Your browser doesn't support speech recognition.");
            return;
        }

        const recognition = new window.webkitSpeechRecognition();
        recognition.continuous = true; // Allow continuous listening
        recognition.interimResults = true; // Capture partial results
        recognition.lang = "en-US";

        recognition.onstart = () => {
            console.log("Voice input started");
            setIsListening(true);
            manuallyStoppedRef.current = false; // Reset manual stop flag
        };

        recognition.onresult = (event) => {
            let finalTranscript = "";
            let interimTranscript = "";

            // Loop through all results (may contain interim results)
            for (let i = event.resultIndex; i < event.results.length; i++) {
                const result = event.results[i];
                if (result.isFinal) {
                    finalTranscript += result[0].transcript.trim(); // Add final transcript
                } else {
                    interimTranscript += result[0].transcript.trim(); // Add interim results
                }
            }

            // If the final transcript changes, update content
            if (finalTranscript && finalTranscript !== lastTranscriptRef.current) {
                lastTranscriptRef.current = finalTranscript;
                setContent((prevContent) => prevContent + " " + finalTranscript);
            }
        };

        recognition.onerror = (event) => {
            console.error("Speech Recognition Error:", event.error);
            setError(`Speech recognition error: ${event.error}`);
        };

        recognition.onend = () => {
            console.log("Voice input stopped");
            setIsListening(false);

            // 🔥 Auto-restart ONLY if the user didn't manually stop it
            if (!manuallyStoppedRef.current) {
                setTimeout(() => {
                    console.log("Restarting speech recognition...");
                    recognition.start();
                }, 500); // Short delay before restarting
            }
        };

        recognitionRef.current = recognition;
    }, []);

    const startListening = () => {
        if (recognitionRef.current && !isListening) {
            manuallyStoppedRef.current = false; // Reset manual stop flag
            recognitionRef.current.start();
            console.log("Started listening...");
        }
    };

    const stopListening = () => {
        if (recognitionRef.current && isListening) {
            manuallyStoppedRef.current = true; // Mark as manually stopped
            recognitionRef.current.stop();
            console.log("Stopped listening.");
        }
    };

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
                onEntryCreated(newEntry);
                setTitle("");
                setContent("");
            } else {
                alert("Failed to create entry. Please try again.");
            }
        } catch (error) {
            console.error("Error creating journal entry:", error);
        }
        // Clear form fields
    setTitle("");
    setContent("");
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

            <button type="button" className="btn btn-info me-2" onClick={startListening} disabled={isListening}>
                {isListening ? "Listening..." : "Start Voice Input"}
            </button>
            <button type="button" className="btn btn-secondary" onClick={stopListening} disabled={!isListening}>
                Stop Listening
            </button>

            {error && <div className="alert alert-danger mt-3">{error}</div>}

            <button type="submit" className="btn btn-primary mt-3" style={{ marginLeft: "16px", marginTop: "5px", marginBottom: "15px" }}>
                Add Entry
            </button>
        </form>
    );
};

export default JournalForm;
