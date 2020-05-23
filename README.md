# Back pressure example

[Related SO post](https://stackoverflow.com/questions/61971389/implement-back-pressure-in-fixedthreadpool)

We have users that submit new tasks. A calculation task runs for some time - around 1 - 4 seconds.
Sometimes, there are can a lot of users submitting tasks faster that the task can finish.

For example, during spikes, there can be 100 users each submitting their task every second, while it needs 3 seconds for a task to finish.
In this case, we have to reject new users' tasks until we finish existing tasks;
otherwise, our system will break at some point because there will be more tasks than our system can handle.

## Implementation details
For now, I use `FixedThreadPool` with `WORKERS_NUMBER` of threads that execute calculations.
