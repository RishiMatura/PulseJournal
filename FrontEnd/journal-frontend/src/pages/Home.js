import { useNavigate } from "react-router-dom";
import { Button, Container, Row, Col, Card } from "react-bootstrap";

function Home() {
    const navigate = useNavigate();

    return (
        <Container className="d-flex justify-content-center align-items-center min-vh-100">
            <Card className="p-5 shadow-lg rounded-4 text-center w-100" style={{ maxWidth: "500px" }}>
                <h1 className="mb-4 text-primary fw-bold">Welcome to the Journal App</h1>
                <p className="text-muted mb-4">Capture your thoughts, reflect on your day, and track your emotions effortlessly.</p>
                <Row className="gap-2 justify-content-center">
                    <Col xs="auto">
                        <Button variant="primary" size="lg" onClick={() => navigate("/login")}>
                            Login
                        </Button>
                    </Col>
                    <Col xs="auto">
                        <Button variant="outline-primary" size="lg" onClick={() => navigate("/signup")}>
                            Sign Up
                        </Button>
                    </Col>
                </Row>
            </Card>
        </Container>
    );
}

export default Home;
