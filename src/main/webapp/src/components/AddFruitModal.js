import React from 'react';
import {Formik} from 'formik';
import Modal from "./Modal";

export default class AddFruitModal extends React.Component {
  
  render() {
    const {handleModalShow, handleModalToggle, handleCreateFruit, fruitToEdit} = this.props;
    const initialValues = {
      id: fruitToEdit ? fruitToEdit.id : 0,
      name: fruitToEdit ? fruitToEdit.name : '',
      description: fruitToEdit ? fruitToEdit.description : '',
    }
    return (
      <Formik
        initialValues={initialValues}
        enableReinitialize
        validate={values => {
          const errors = {};
          if (!values.name) {
            errors.name = 'Required';
          } else if (!values.description) {
            errors.description = 'Required';
          }
          return errors;
        }}
        onSubmit={(values, {setSubmitting}) => {
          console.log("Values to submit: ", values);
          setTimeout(() => {
            handleCreateFruit(values);
            setSubmitting(false);
          }, 400);
        }}
      >
        {({
            values,
            errors,
            touched,
            handleChange,
            handleBlur,
            handleSubmit,
            isSubmitting,
          }) => (
          
          <Modal
            title="Add/Update fruit"
            onClose={handleModalToggle}
            show={handleModalShow}
            modalBody={() => {
              return (
                <form onSubmit={handleSubmit}>
                  <input type="hidden" name="id" value={values.id}/>
                  
                  <input type="text"
                         name="name"
                         onChange={handleChange}
                         onBlur={handleBlur}
                         value={values.name}/>
                  {errors.name && touched.name && errors.name}
                  <input
                    type="description"
                    name="description"
                    onChange={handleChange}
                    onBlur={handleBlur}
                    value={values.description}
                  />
                  {errors.description && touched.description && errors.description}
                  <button type="submit" disabled={isSubmitting}>
                    Submit
                  </button>
                </form>
              )
            }}
          />
        )}
      </Formik>
    );
  }
}