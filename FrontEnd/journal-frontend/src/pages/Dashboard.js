import { useEffect, useState } from "react";
import JournalForm from "../components/JournalForm";
import EditJournalModal from "../components/EditJournalModal";

function Dashboard() {
    const [entries, setEntries] = useState([]);
    const [editingEntry, setEditingEntry] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);

    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    const fetchEntries = () => {
        fetch("http://localhost:8080/journal/getAll", {
            method: "GET",
            headers: {
                Authorization: "Basic " + btoa(`${username}:${password}`),
                "Content-Type": "application/json",
            },
        })
            .then((response) => response.json())
            .then((data) => setEntries(data))
            .catch((error) => console.error("Error fetching journal entries:", error));
    };

    useEffect(() => {
        fetchEntries();
    }, []);

    const handleEntryCreated = () => fetchEntries();

    const handleUpdateSuccess = (updatedEntry) => {
        setEntries((prevEntries) =>
            prevEntries.map((e) => (e.id === updatedEntry.id ? updatedEntry : e))
        );
    };

    const getEmotionBadgeClass = (emotion) => {
        if (!emotion) return "bg-secondary";
        const lower = emotion.toLowerCase();
        if (lower === "happy" || lower === "positive") return "bg-success";
        if (lower === "sad" || lower === "negative") return "bg-warning";
        return "bg-info";
    };

    const handleLogout = async () => {
        try {
            await fetch("http://localhost:8080/auth/logout", {
                method: "POST",
                headers: {
                    Authorization: "Basic " + btoa(`${username}:${password}`),
                    "Content-Type": "application/json",
                },
            });

            localStorage.removeItem("username");
            localStorage.removeItem("password");

            // Redirect to login page
            window.location.href = "/login";
        } catch (err) {
            console.error("Logout failed:", err);
        }
    };

    return (
        <div
            style={{
                backgroundImage: `url('/assets/background-floral.jpg')`,
                backgroundSize: "cover",
                backgroundPosition: "center",
                backgroundAttachment: "fixed",
                minHeight: "100vh",
                padding: "60px 0",
                position: "relative",
                fontFamily: "'Segoe UI', sans-serif"
            }}
        >
            {/* Soft overlay to blend background (only once, not on cards!) */}
            <div style={{
                position: "absolute",
                top: 0,
                left: 0,
                width: "100%",
                height: "100%",
                backgroundColor: "rgba(255, 255, 255, 0.6)",
                zIndex: 0
            }}></div>

            <div className="container position-relative" style={{ zIndex: 1 }}>
                <div className="text-center mb-4">
                    <h2 className="fw-bold text-dark display-5">
                        Hello, <span className="text-primary">{username}</span> ✨
                    </h2>
                    <p className="text-muted fs-5">This is your sanctuary of words and emotions.</p>
                    
                    {/* 🔒 Logout Button */}
                    <button
                        className="btn btn-outline-danger rounded-pill px-4 py-2 fw-semibold shadow-sm mt-3"
                        onClick={handleLogout}
                        style={{ transition: "all 0.3s ease", fontSize: "1rem" }}
                    >
                        🔒 Logout
                    </button>
                </div>

                <div className="mb-5">
                    <JournalForm onEntryCreated={handleEntryCreated} />
                </div>

                <h4 className="fw-semibold mb-4 text-dark">📖 Your Journal Entries</h4>

                {entries.length === 0 ? (
                    <div className="alert alert-info shadow rounded-4 text-center">
                        No journal entries yet. Start writing your story.
                    </div>
                ) : (
                    <div className="row g-4">
                        {entries.map((entry, index) => (
                            <div className="col-md-6 col-lg-4" key={index}>
                                <div
                                    className="card border-0 h-100 rounded-4 shadow-sm"
                                    style={{
                                        background: "#ffffffcc",
                                        transition: "transform 0.2s ease-in-out",
                                        cursor: "pointer"
                                    }}
                                    onMouseEnter={(e) => e.currentTarget.style.transform = "scale(1.015)"}
                                    onMouseLeave={(e) => e.currentTarget.style.transform = "scale(1)"}
                                >
                                    <div className="card-body d-flex flex-column">
                                        <h5 className="card-title text-primary fw-bold">{entry.title}</h5>
                                        <p className="text-muted small mb-2">
                                            {new Date(entry.date).toLocaleDateString()} • Owner: {entry.owner}
                                        </p>
                                        <p className="card-text mb-3 text-dark" style={{ fontSize: "0.95rem" }}>
                                            {entry.content}
                                        </p>
                                        <div className="mt-auto d-flex justify-content-between align-items-center">
                                            <span className={`badge ${getEmotionBadgeClass(entry.emotion)} rounded-pill px-3 py-2`}>
                                                {entry.emotion || "N/A"}
                                            </span>
                                            <button
                                                className="btn btn-outline-primary btn-sm rounded-pill px-3"
                                                onClick={() => {
                                                    setEditingEntry(entry);
                                                    setShowEditModal(true);
                                                }}
                                            >
                                                ✏️ Edit
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {editingEntry && (
                    <EditJournalModal
                        show={showEditModal}
                        handleClose={() => setShowEditModal(false)}
                        entry={editingEntry}
                        onUpdateSuccess={handleUpdateSuccess}
                    />
                )}
            </div>
        </div>
    );
}

export default Dashboard;
