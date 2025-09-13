<script>
  import CreatePoll from './components/CreatePoll.svelte';
  import Vote from './components/Vote.svelte';
  let tab = 'create';
</script>

<center>
<h1>PollApp</h1>

<button on:click={() => (tab = 'create')}>Create a poll</button>
<button on:click={() => (tab = 'vote')}>Vote on a poll</button>
</center>

{#if tab === 'create'} <CreatePoll /> {/if}
{#if tab === 'vote'}   <Vote /> {/if}
