# Dinning_Philosopher_1DV512

a Java application that simulates the
Dining Philosophers problem:

Five philosophers sit around a table and in front of each philosopher there is a bowl of food.

A philosopher can be in three states: EATING, THINKING, or HUNGRY. 

There are five chopsticks available.

A hungry philosopher needs two chopsticks (his left and right chopstick) for eating.

If a chopstick is used by a neighbor, then the philosopher must wait until the chopstick is released. 
The philosopher first picks up the left chopstick, and then the right chopstick.

Initially all five philosophers are in THINKING state. 

Time for THINKING and EATING is generated pseudo-randomly from a discrete uniform distribution [0, 1000].

Basically, a philosopher is in THINKING or EATING state for 0-1000 time units (milliseconds).

One of these values is selectedin pseudo-random2 manner to simulate thinkingor eating time.
