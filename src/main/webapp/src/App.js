import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Provider} from "react-redux";
import store from "./store/store";
import Menu from "./commons/Menu";
import{
  BrowserRouter as Router,
  Route, Switch } from "react-router-dom";
import Fruit from "./containers/Fruit";
import Legume from "./containers/Legume";
import Recipe from "./containers/Recipe";

function App() {
  return (
    <React.Fragment>
      <Provider store={store}>
        <Router path={"/"}>
          <Menu />
          <div className="container-fluid">
            <Switch>
              <Route path="/" exact component={Home} />
              <Route path="/fruits" exact component={Fruit} />
              <Route path="/legumes" exact component={Legume} />
              <Route path="/recipes" exact component={Recipe} />
              <Route render={() => <h1>404: page not found</h1>} />
            </Switch>
          </div>
        </Router>
      </Provider>
    </React.Fragment>
  );
}

export default App;

// Home Page
const Home = () => (
  <React.Fragment>
    <h1>Home</h1>
  </React.Fragment>
);
