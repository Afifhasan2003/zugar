# zugar
A peer-to-peer renting system

# Peer-to-Peer Rental Marketplace

A JavaFX-based peer-to-peer rental platform where users can list their personal belongings for rent and other users can discover, negotiate, and rent those items for a specific period.

The system is designed around real-world rental workflows such as availability management, owner approval, price negotiation, access restrictions, security deposits, compensation, item condition tracking, and rental bundles.

---

## Project Overview

People often own items that they do not use regularly, while others may need those items temporarily.

For example, a person may own a camera, microphone, tripod, laptop, projector, or other equipment. Instead of leaving the item unused, the owner can list it on the platform for rent.

A renter can search for the required item, check its availability and eligibility, submit a rental request, negotiate the price with the owner, and rent the item after approval.

The platform is intended to support local and trusted rental communities such as universities, housing societies, organizations, or general users.

---

## Core Features

### 1. Item Listing

Users can list their belongings for rent.

Each listing may contain:

- Item name
- Category
- Description
- Photos
- Suggested rental price
- Security deposit
- Compensation amount
- Current condition
- Availability
- Location
- Access restrictions

Owners can also update or remove their listings.

---

### 2. Search and Filtering

Users can search for items they need.

Search results can be filtered based on:

- Category
- Price
- Availability
- Location
- Access eligibility

Search results are divided into two sections:

#### Directly Available

Items that the user is currently eligible to rent.

#### Request Access

Items that exist but have access restrictions.

For example:

> Camera - Available  
> Restricted to University of Dhaka members

A user who does not satisfy the restriction can still request access from the owner.

---

### 3. Access Restrictions

Owners can control who can normally rent their items.

Possible restrictions include:

- Anyone
- University members
- Housing society members
- Organization members
- Other verified groups

For example, an owner may choose:

> "Only members of my university can directly rent this item."

Users outside the restriction can send an access request and explain their purpose.

The owner can then decide whether to allow the exception.

---

### 4. Access Requests

When a user finds a restricted item, they can submit an access request.

The request can contain:

- Reason for requesting access
- Intended rental dates
- Proposed rental price
- Additional message

The owner can:

- Accept the access request
- Reject the request

If accepted, the user becomes eligible to proceed with the rental request for that item.

---

### 5. Owner Approval

A rental request does not automatically become a confirmed rental.

The renter submits a request containing:

- Rental dates
- Number of days
- Offered price
- Additional information

The owner reviews the request and can:

- Accept
- Reject
- Counter-offer

This gives the owner control over who rents their belongings.

---

### 6. Price Negotiation

Each item has a suggested rental price, but the renter can propose a different price.

Example:

```text
Suggested price: ৳1,000/day

Rental duration: 3 days

Renter's offer: ৳2,400 total
```