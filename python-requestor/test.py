#!/usr/bin/env python3
import asyncio
import os
from datetime import datetime
from typing import AsyncIterable
from typing import Optional

from yapapi import Golem, Task, WorkContext
from yapapi import rest
from yapapi.log import enable_default_logger
from yapapi.payload import vm
from yapapi.strategy import (
    MarketStrategy, SCORE_TRUSTED,
    SCORE_REJECTED, LeastExpensiveLinearPayuMS
)

# providers tested in current run
provider_ids = {}
# providers tested in previous runs, no need to test them again
tested_providers = []


class MyStrategy(MarketStrategy):

    async def score_offer(
            self, offer: rest.market.OfferProposal
    ) -> float:
        """Score `offer`. Better offers should get higher scores."""
        # now = datetime.now()
        # current_time = now.strftime("%H:%M:%S")
        # print("Current Time =", current_time)
        # print(f"offer id: {offer.id}")
        # print(f"offer issuer: {offer.issuer}")
        # print("props coeffs")
        # print(f'props: {offer.props["golem.com.pricing.model.linear.coeffs"]}')
        if offer.issuer in tested_providers:
            return SCORE_REJECTED

        p = offer.props["golem.com.pricing.model.linear.coeffs"]
        # test only inexpensive providers
        #if p[0] > 1.0e-4:
        #    return SCORE_REJECTED
        #if p[1] > 1.0e-4:
        #    return SCORE_REJECTED
        #if p[2] > 1.0e-4:
        #    return SCORE_REJECTED

        # provider name -> id 
        provider_ids[offer.props["golem.node.id.name"]] = offer.issuer
        return SCORE_TRUSTED


async def worker(context: WorkContext, tasks: AsyncIterable[Task]):
    async for task in tasks:
        provider_id = provider_ids[context.provider_name]
        print(f'provider_name: {context.provider_name}')
        print(f'provider_id: {provider_id}')
        tested_providers.append(provider_ids[context.provider_name])
        os.system(f"date >> start.txt")
        os.system(f"echo {provider_id} >> start.txt")
        script = context.new_script()
        script.run("/bin/sh", "-c", f"printf \"{provider_id}\n\" >> geek.txt")
        script.run("/bin/sh", "-c", "sysbench --test=cpu --cpu-max-prime=10000 run | grep \"events per second\" >> geek.txt")
        script.run("/bin/sh", "-c", "sysbench --test=cpu --cpu-max-prime=10000 --threads=`nproc` run | grep \"events per second\" >> geek.txt")
        # context.run("/bin/sh", "-c", "7z b >> geek.txt")
        script.run("/bin/sh", "-c", "ls -la /golem/work/")
        output_file = "geek2.txt"
        script.run("/bin/sh", "-c", "mv /golem/work/*.* /golem/output")
        future_results = script.download_file("/golem/output/geek.txt", output_file)

        yield script
        results = await future_results
        os.system(f"cat {output_file} >> geek.txt")
        os.system(f"date >> finish.txt")
        os.system(f"echo {provider_id} >> finish.txt")

        #        context.download_file("/golem/work/mbf9exact/data/done/data7828353.bin", output_file)
        #        yield context.commit(timeout=timedelta(minutes=1))
        #        results = await future_results
        task.accept_result(result=output_file)
        # task.accept_result(result=results[-1])
        # task.accept_result(true)

async def main():
    package = await vm.repo(
        image_hash="dc2b2a34df7c6a00f824cebef74379c4c73e4dba1f8e8d0cd139fe36",
    )

    tasks = [Task(data=None)]

    async with Golem(budget=5.0e-2, strategy=MyStrategy()) as golem:
        async for completed in golem.execute_tasks(worker, tasks, payload=package):
            print(completed.result)


if __name__ == "__main__":
    enable_default_logger(log_file="hello.log")
    i = 1
    while i < 20:
        loop = asyncio.get_event_loop()
        task = loop.create_task(main())
        loop.run_until_complete(task)
