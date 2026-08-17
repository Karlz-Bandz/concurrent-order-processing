# Concurrent Order Processing

A simple Java project demonstrating concurrent order processing using
`BlockingQueue` and `ExecutorService`.

## Overview

The application simulates an order processing system where multiple workers
process orders concurrently and reserve products from a shared inventory.

## Technologies

- Java
- Multithreading
- BlockingQueue
- ExecutorService
- synchronized
- Builder Pattern

## How it works

1. Orders are added to a `BlockingQueue`.
2. Multiple `OrderWorker` instances consume orders concurrently.
3. Each worker tries to reserve the requested quantity from the shared inventory.
4. `Inventory.reserve()` is synchronized to prevent race conditions.
5. `STOP` orders are used to gracefully terminate workers.
6. The application prints the processing result and final inventory state.

## Example

```text
Initial stock: {1=10, 2=5, 3=2}
pool-1-thread-2 | Order: 2 | Product: 1 | Quantity: 3 | Result: true
pool-1-thread-1 | Order: 1 | Product: 1 | Quantity: 3 | Result: true
pool-1-thread-3 | Order: 3 | Product: 3 | Quantity: 12 | Result: false
pool-1-thread-4 | Order: 2 | Product: 3 | Quantity: 2 | Result: true
pool-1-thread-2 received stop!
pool-1-thread-1 received stop!
pool-1-thread-4 received stop!
pool-1-thread-3 received stop!
Time: 19 ms
{1=4, 2=5, 3=0}
