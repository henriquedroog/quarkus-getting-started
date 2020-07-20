import React from 'react';
import {Jumbotron, Table, Navbar, Nav, Button, DropdownButton, ButtonGroup, Dropdown} from 'react-bootstrap';
import AddFruitModal from '../components/AddFruitModal';
import { FRUITS_API_URL } from '../commons/config';
import { FileMinus, PencilSquare } from 'react-bootstrap-icons';

class Fruit extends React.Component {
  
  state = {
    fruits: [],
    modalShow: false,
    fruit: {},
  }
  
  componentDidMount() {
    fetch(FRUITS_API_URL)
    .then(res => res.json())
    .then((data) => {
      this.setState({fruits: data})
    })
    .catch(console.log);
  }
  
  handleCreateFruit = (data) => {
    const  requestOptions = {
      method: data.id && data.id > 0 ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    };
    
    fetch(FRUITS_API_URL, requestOptions)
      .then(response => response.json())
      .then(data => {
        this.setState({fruits: data});
        this.handleModalToggle(false);
      })
      .catch(console.log);
  }
  
  handleModalToggle = (isOpen) => {
    this.setState({ modalShow: isOpen });
  };
  
  handleAddNewFruit = () => {
    this.handleModalToggle(true);
  };
  
  handleDeleteFruit = (fruitId) => {
    const  requestOptions = {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json'},
      body: JSON.stringify(fruitId)
    };
    fetch(FRUITS_API_URL, requestOptions)
    .then(response => response.json())
    .then(data => {
      this.setState({fruits: data});
    })
    .catch(console.log);
  }
  
  fetchFruitById = (fruitId) => {
    fetch(FRUITS_API_URL + '/' + fruitId)
    .then(res => res.json())
    .then((data) => {
      console.log("Data: ", data);
      this.setState({fruit: data})
    })
    .catch(console.log);
    this.handleModalToggle(true);
  };
  
  render() {
    const { fruits, modalShow, fruit } = this.state;
    return (
      <Jumbotron>
        <AddFruitModal
          handleModalShow={modalShow}
          handleModalToggle={this.handleModalToggle}
          handleCreateFruit={this.handleCreateFruit}
          fruitToEdit={fruit}
        />
        <h1>Fruits list</h1>
        <Navbar bg="light" expand="lg">
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
              <Button variant="primary" onClick={this.handleAddNewFruit}>Add new fruit</Button>
            </Nav>
          </Navbar.Collapse>
        </Navbar>
        <Table striped bordered hover>
          <thead>
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          {fruits && fruits.map((fruit, index) => {
            return (
              <tr key={index}>
                 <td>{fruit.id}</td>
                 <td>{fruit.name}</td>
                 <td>{fruit.description}</td>
                <td>
                  <DropdownButton as={ButtonGroup}
                                  key={'Primary'}
                                  title={'Actions'}>
                    <Dropdown.Item eventKey="1" onClick={() => this.fetchFruitById(fruit.id)}>
                      <PencilSquare /> Edit
                    </Dropdown.Item>
                    <Dropdown.Item eventKey="2" onClick={() => this.handleDeleteFruit(fruit.id)}>
                      <FileMinus /> Remove
                    </Dropdown.Item>
                  </DropdownButton>
                </td>
              </tr>
            )
          })}
          </tbody>
        </Table>
      </Jumbotron>
    )
  }
}

export default Fruit;