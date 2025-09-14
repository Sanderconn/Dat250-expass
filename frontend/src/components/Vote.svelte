<script>
  import { onMount } from 'svelte';

  let phase = 'pick';
  let polls = [];
  let selectedPollId = '';
  $: selectedPoll = polls.find(p => p.id === selectedPollId);
  let status = '';
  const userId = 2;

  onMount(loadPolls);

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
      await loadPolls();
      selectedPollId = id;

      status = 'Vote recorded';
    } catch {
      status = 'Failed to submit vote';
    }
  }

  function back(){ phase='pick'; selectedPollId=''; }
</script>

<div style="text-align:center; margin-bottom:1rem;">
{#if phase === 'pick'}
  <h2>Select a poll</h2>
  {#if status}<p>{status}</p>{/if}
    {#each polls as p (p.id)}
      <li>{p.question} <button on:click={() => choose(p.id)}>choose</button></li>
    {/each}
{/if}

{#if phase === 'vote'}
  <h2>{selectedPoll.question}</h2>
  {#if status}<p>{status}</p>{/if}
    {#each (selectedPoll.voteOptions ?? []) as opt (opt.id ?? opt.optionId)}
      <li>
        {opt.caption}
        <button on:click={() => submitVote(opt.id ?? opt.optionId)}>vote!</button>
        {#if opt.votes != null}
          <span> — {opt.votes} {opt.votes === 1 ? 'vote' : 'votes'}</span>
        {/if}
      </li>
    {/each}
  <button on:click={back}>Go Back</button>
{/if}
</div>