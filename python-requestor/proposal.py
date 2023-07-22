#!/usr/bin/env python3
import asyncio
from datetime import datetime
from typing import AsyncIterable
from typing import Optional

from yapapi import Golem, Task, WorkContext
from yapapi import rest
from yapapi.log import enable_default_logger
from yapapi.payload import vm
from yapapi.strategy import (
    MarketStrategy, SCORE_NEUTRAL, SCORE_TRUSTED,
    SCORE_REJECTED
)


class MyStrategy(MarketStrategy):

    async def score_offer(
            self, offer: rest.market.OfferProposal
    ) -> float:
        """Score `offer`. Better offers should get higher scores."""
        now = datetime.now()
        current_time = now.strftime("%H:%M:%S")
        print("Current Time =", current_time)
        print(f"offer id: {offer.id}")
        print(f"offer issuer: {offer.issuer}")
        print(f"offer props: {offer.props}")
        # if offer.issuer == "0xa4eb429ff594dd59753d6994c14928b1b994175c":
        #    return SCORE_TRUSTED
        return SCORE_REJECTED


async def worker(context: WorkContext, tasks: AsyncIterable[Task]):
    async for task in tasks:
        # context.run("/bin/sh", "-c", "date")
        context.run("/bin/sh", "-c", "cat /proc/cpuinfo | grep model")

        future_results = yield context.commit()
        results = await future_results
        task.accept_result(result=results[-1])


async def main():
    package = await vm.repo(
        image_hash="d646d7b93083d817846c2ae5c62c72ca0507782385a2e29291a3d376",
    )

    tasks = [Task(data=None)]
    #tasks = [Task(data=x) for x in range(3)]

    async with Golem(budget=1e-12, strategy=MyStrategy()) as golem:
        async for completed in golem.execute_tasks(worker, tasks, payload=package):
            print(completed.result.stdout)
            print(completed.id)
            print(completed.data)
            print(completed.running_time)


if __name__ == "__main__":
    enable_default_logger(log_file="hello.log")

    loop = asyncio.get_event_loop()
    task = loop.create_task(main())
    loop.run_until_complete(task)
