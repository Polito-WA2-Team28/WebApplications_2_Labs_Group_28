export class Ticket{
    constructor(id, state, customer, expert, product, messages, creationDate, lastModified) {
        this.id = id;
        this.state = state;
        this.customer = customer;
        this.expert = expert;
        this.product = product;
        this.messages = messages;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }
}

