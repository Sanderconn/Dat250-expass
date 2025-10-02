<script>
  import { onMount } from 'svelte';

  let phase = 'pick';
  let polls = [];
  let selectedPollId = '';
  $: selectedPoll = polls.find(p => p.id === selectedPollId);
  let status = '';
  const userId = 2;

  onMount(loadPolls);

  async function applyCounts(pollId) {
  const r = await fetch(`/api/polls/${pollId}/results`);
  if (!r.ok) return;
  const counts = await r.json();

  const p = polls.find(p => p.id === pollId);
  if (!p || !p.voteOptions) return;

  for (const opt of p.voteOptions) {
    opt.votes = Number(counts[opt.id] ?? 0);
  }
  polls = [...polls];
}

  async function loadPolls() {
    status = 'Loading…';
    try {
      const res = await fetch('/api/polls');
      if (!res.ok) throw new Error();
      polls = await res.json();          
      status = '';
    } catch {
      status = 'Failed to load polls';
    }
  }
  function choose(id) {
    selectedPollId = id;
    phase = 'vote';
    status = '';
    applyCounts(id);
  }

  async function submitVote(optionId) {
    if (!selectedPollId) return;
    status = 'Submitting vote…';
    try {
      const res = await fetch(`/api/polls/${selectedPollId}/votes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId, optionId })
      });
      if (!res.ok) throw new Error();
      const id = selectedPollId;
      //await loadPolls();
      selectedPollId = id;

      await applyCounts(selectedPollId);
      status = 'Vote recorded';
    } catch {
      status = 'Failed to submit vote';
    }
  }

  function back(){ phase='pick'; selectedPollId=''; status = '';}
</script>

{#if phase === 'pick'}
    <div class = "card box">
        <h2>Select a poll</h2>
        <div class = "list">
            {#each polls as p (p.id)}
                <button class = "btn" on:click={() => choose(p.id)}>{p.question}</button>
            {/each}
        </div>
        {#if status}<p class = "status">{status}</p>{/if}
    </div>
{/if}

{#if phase === 'vote'}
    <div class = "card box">
        <h2>{selectedPoll.question}</h2>
        {#each (selectedPoll.voteOptions ?? []) as opt (opt.id ?? opt.optionId)}
        <div class = "options">
            {opt.caption}
            <button class = "btn save" on:click={() => submitVote(opt.id ?? opt.optionId)}>vote!</button>
            {#if opt.votes != null}
            <span> — {opt.votes} {opt.votes === 1 ? 'vote' : 'votes'}</span>
            {/if}
        </div>
    {/each}
    <button class = "btn" on:click={back}>Go Back</button>
    </div>
    {#if status}<p class = "status">{status}</p>{/if}
{/if}

<style>
    h2 { margin: 0; text-align: center; }

    .card {
        max-width: 700px;
        margin: 1.5rem auto;
        padding: 1rem;
        border: 1px solid #efefef;
        border-radius: 12px;
    }

    .list {
        display: flex;
        flex-wrap: wrap;
        gap: 0.5rem;  
    }

    .btn {
        padding: 0.5rem;
        border-radius: 8px;
        border: 1px solid #c7c7c7;
        background: #ffffff;
        cursor: pointer;
    }

    .list .btn {
        font-size: 1rem;
        line-height: 1.3;
        text-align: left;
    }

    .options {
        padding-top: 0.4rem;
        text-align: center;
    }
  .btn:hover { filter: brightness(0.9); }
  .save { background: #a9f6b4;}
  .box {background: #eeeeee;}

  .status {
    text-align: center; 
  }
</style>