import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import {Link} from "react-router-dom";
export default function Menu ({
  activeMenu,
}) {
  return (
    <Navbar bg="light" expand="lg" className="navbar navbar-expand-md navbar-dark bg-dark mb-3">
      <Container fluid>
        <Link to="/" className="navbar-brand mr-3">Quarkus IO POC</Link>
        <button type="button" className="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
          <span className="navbar-toggler-icon"></span>
        </button>
  
        <div className="collapse navbar-collapse" id="navbarCollapse">
          <div className="navbar-nav">
              <Link to="/fruits" className="nav-item nav-link active">Fruits</Link>
          </div>
          <div className="navbar-nav ml-auto">
            <a href="#" className="nav-item nav-link">Register</a>
            <a href="#" className="nav-item nav-link">Login</a>
          </div>
        </div>
      </Container>
    </Navbar>
  )
}