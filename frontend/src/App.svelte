<script>
  import CreatePoll from './components/CreatePoll.svelte';
  import Vote from './components/Vote.svelte';
  import Login from './components/Login.svelte';
  import CreateUser from './components/CreateUser.svelte';
  let tab = 'create';
</script>

<div class="app">
  <h1>PollApp</h1>
  {#if tab === 'create' || tab === 'vote'}
    <div class="toolbar">
      <button class="btn tab" class:active={tab === 'create'} on:click={() => (tab = 'create')}>
        Create a poll
      </button>
      <button class="btn tab" class:active={tab === 'vote'} on:click={() => (tab = 'vote')}>
        Vote on a poll
      </button>
    </div>
    <div class="toolbar">
      <button class="btn tab" on:click={() => (tab = 'login')}>
        Log out
      </button>
    </div>
  {/if}

  {#if tab === 'login'}
    <div class="toolbar">
      <button class="btn tab" on:click={() => (tab = 'create')}>
        Continue as guest
      </button>
      <button class="btn tab" on:click={() => (tab = 'createUser')}>
        Create user
      </button>
      <button class="btn tab" on:click={() => (tab = 'create')}>
        Login
      </button>
    </div>
  {/if}

  {#if tab === 'createUser'}
    <div class="toolbar">
      <button class="btn tab" on:click={() => (tab = 'login')}>
        Back to login
      </button>
      <button class="btn tab" on:click={() => (tab = 'create')}>
        Log in
      </button>
    </div>
  {/if}


  {#if tab === 'login'}   <Login /> {/if}
  {#if tab === 'createUser'}   <CreateUser /> {/if}
  {#if tab === 'create'} <CreatePoll /> {/if}
  {#if tab === 'vote'}   <Vote /> {/if}
</div>

<style>
  .app {
    max-width: 760px;
    margin: 1.5rem auto;
    padding: 0 1rem;
  }

  h1 {
    text-align: center;
    margin: 0.2rem;
  }

  .toolbar {
    display: flex;
    justify-content: center;
    gap: 0.5rem;
    margin-bottom: 1rem;
  }

  .btn {
    padding: 0.5rem;
    border-radius: 8px;
    border: 1px solid #c7c7c7;
    background: #fff;
    cursor: pointer;
  }
  .btn:hover { filter: brightness(0.9); }
  .tab.active {background: #eeeeee;}
</style>
