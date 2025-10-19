<script>
  export let onCreated = (user) => {};
  export let onCancel = () => {};

  let username = '';
  let email = '';
  let loading = false;
  let err = '';

  async function submit() {
    loading = true; err = '';
    try {
      const res = await fetch('/api/users', {
        method: 'POST',
        headers: { 'Content-Type':'application/json' },
        body: JSON.stringify({ username, email })
      });
      if (!res.ok) throw new Error();
      const user = await res.json();
      onCreated(user);
    } catch {
      err = 'Failed to create user';
    } finally {
      loading = false;
    }
  }
</script>

<div class="card box">
  <h2>Create User</h2>
  <div class="field"><input class="input" placeholder="Username" bind:value={username} /></div>
  <div class="field"><input class="input" type="email" placeholder="Email (optional)" bind:value={email} /></div>
  <div class="buttonField">
    <button class="btn save" on:click={submit}> save</button>
    <button class="btn" on:click={onCancel}>Cancel</button>
  </div>
  {#if err}<p class="status">{err}</p>{/if}
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

  .btn:hover{filter:brightness(.9)}
  .save{background:#a9f6b4}
  .box{background:#eee}
  .status{text-align:center}
</style>
