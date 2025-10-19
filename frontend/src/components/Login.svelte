<script>
  export let onSuccess = (user) => {};
  export let onGotoCreateUser = () => {};
  export let onGuest = (user) => {};

  let username = '';
  let email = '';
  let err = '';

  async function login() {
    err = '';
    try {
      const res = await fetch('/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email })
      });
      if (res.status === 404) { err = 'No user found. Create one?'; return; }
      if (!res.ok) throw new Error();
      const user = await res.json();
      onSuccess(user);
    } catch {
      err = 'Login failed';
    }
  }

</script>

<div class="card box">
  <h2>Log in</h2>
  <form on:submit|preventDefault={login}>
    <div class="field"><input class="input" placeholder="Username" bind:value={username} /></div>
    <div class="field"><input class="input" type="email" placeholder="Email (optional)" bind:value={email} /></div>
    <div class="buttonField">
      <button class="btn save">Log in</button>
      <button class="btn white" type="button" on:click={onGotoCreateUser}>Create user</button>
      <button class="btn" type="button" on:click={() => onGuest({ id: 0, username: 'Guest', guest: true })}>
        Continue as guest
      </button>
    </div>
    {#if err}<p class="status">{err}</p>{/if}
  </form>
</div>


<style>
  h2{margin:0;text-align:center}
  
  .card {
        max-width: 560px;
        margin: 1.5rem auto;
        padding: 1rem;
        border: 1px solid #efefef;
        border-radius: 12px;
    }

  .field{margin:.5rem 0}
  
  .input {
    width: 95%;
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 8px;
    font-size: 1rem;
  }
  
  .buttonField {
    display: flex;
    gap: 0.5rem;
    justify-content: center;
    margin-top: 0.75rem;
  }

  .btn {
    padding: 0.5rem;
    border-radius: 8px;
    border: 1px solid #c7c7c7;
    background: #fff;
    cursor: pointer;
  }
  .save{background:#a9f6b4}
  .white{background:#fff}
  .box{background:#eee}
  .status{text-align:center}
</style>
