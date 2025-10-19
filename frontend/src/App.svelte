<script>
  import CreatePoll from './components/CreatePoll.svelte';
  import Vote from './components/Vote.svelte';
  import Login from './components/Login.svelte';
  import CreateUser from './components/CreateUser.svelte';

  let tab = 'login';
  let currentUser = null;

  function onLoggedIn(user) { currentUser = user; tab = 'create'; }
  function onGuest(user)    { currentUser = user; tab = 'create'; }
  function gotoCreateUser() { tab = 'createUser'; }
  function onUserCreated(u) { currentUser = u; tab = 'create'; }
  function onCancelCreate() { tab = 'login'; }
  function logout()         { currentUser = null; tab = 'login'; }
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
      <span style="align-self:center">
        {#if currentUser.guest}
        Hi, Guest!
        {:else}
        Hi, {currentUser.username}
        {/if}
      </span>
      <button class="btn tab" on:click={logout}>Log out</button>
    </div>
  {/if}

  {#if tab === 'login'}
    <Login
      onSuccess={onLoggedIn}
      onGuest={onGuest}
      onGotoCreateUser={gotoCreateUser}
    />
  {/if}

  {#if tab === 'createUser'}
    <CreateUser
      onCreated={onUserCreated}
      onCancel={onCancelCreate}
    />
  {/if}

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
