import { useContext, useState } from 'react'
import { Button, Form, Modal } from 'react-bootstrap'
import { useParams } from 'react-router-dom'
import { ActionContext } from '../Context'

export default function ProductPage() {
  const { productId } = useParams()
  const [product, setProduct] = useState(null)
  const [show, setShow] = useState(false)
  const { getProductByID, handleCreateTicket } = useContext(ActionContext)

  product == null &&
    getProductByID(productId).then((product) => {
      setProduct(product)
    })

  return (
    <div>
      <h1>Product Page</h1>
      {product == null ?  <h2>Loading</h2> : 
        <>
          {console.log(product)}
          <h2>PRODUCT LOADED</h2>
          <OpenNewTicketModal
            show={show}
            handleClose={() => setShow(false)}
              handleCreate={handleCreateTicket}
              product={product}
          />
          <Button onClick={() => setShow(true)}>Open a ticket</Button>
        </>
      }
    </div>
  )
}

function OpenNewTicketModal(props) {
  const [description, setDescription] = useState('')
  const serialNumber = props.product.serialNumber

  console.log(props)

  const handleCreate = () => {
    const ticket = { description, serialNumber }
    props.handleCreate(ticket)
    props.handleClose()
  }

  return (
    <Modal show={props.show} onHide={props.handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Create a ticket for {props.product.serialNumber}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
            <Form.Label>Description</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter description"
              value={description}
              onChange={(ev) => setDescription(ev.target.value)}
            />
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={props.handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={handleCreate}>
          Create
        </Button>
      </Modal.Footer>
    </Modal>
  )
}
