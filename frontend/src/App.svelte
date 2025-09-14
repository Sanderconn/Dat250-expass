<script>
  import CreatePoll from './components/CreatePoll.svelte';
  import Vote from './components/Vote.svelte';
  let tab = 'create';
</script>

<h1 style="text-align:center;">PollApp</h1>

<div style="text-align:center; margin-bottom:1rem;">
  <button on:click={() => (tab = 'create')}>Create a poll</button>
  <button on:click={() => (tab = 'vote')}>Vote on a poll</button>
</div>

{#if tab === 'create'} <CreatePoll /> {/if}
{#if tab === 'vote'}   <Vote /> {/if}
