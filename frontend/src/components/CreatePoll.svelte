<script>
  let question = '';
  let optionCaptions = ['',''];
  let status = '';
  const creatorId = 1;

  function addOption(){ optionCaptions = [...optionCaptions, '']; }
  function reset(){ question=''; optionCaptions=['','']; status = '';}

  async function submit(){
    const clean = optionCaptions.map(s=>s.trim()).filter(Boolean);
    if(!question.trim() || clean.length < 2){ status='Needs a question and at least 2 options'; return; }

    const now = new Date();
    const payload = {
      creatorId,
      question,
      publishedAt: now.toISOString(),
      validUntil: new Date(now.getTime()+7*24*60*60*1000).toISOString(),
      voteOptions: clean.map((caption,i)=>({ caption, presentationOrder: i+1 }))
    };

    status = 'Saving…';
    const res = await fetch('/api/polls', {
      method:'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify(payload)
    });
    if(!res.ok){ status='Failed to create'; return; }

    reset();
    status = 'Created poll!';
  }
</script>

<div class="card box">
  <h2>Create Poll</h2>

  <div class="field">
    <input class="input" placeholder="Question" bind:value={question} />
  </div>

  {#each optionCaptions as _, i}
    <div class="field">
      <input class="input" placeholder={`Option ${i+1}`} bind:value={optionCaptions[i]} />
    </div>
  {/each}

  <div class="buttonField">
    <button class="btn reset" on:click={reset}>Reset form</button>
    <button class="btn white" on:click={addOption}>Add option</button>
    <button class="btn save" on:click={submit}>Save</button>
  </div>
</div>

{#if status}<p class="status">{status}</p>{/if}

<style>
    h2 { margin: 0; text-align: center; }

    .card {
        max-width: 560px;
        margin: 1.5rem auto;
        padding: 1rem;
        border: 1px solid #efefef;
        border-radius: 12px;
    }
    
  .field { margin: 0.5rem 0; }

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
    background: #ffffff;
    cursor: pointer;
  }
  .btn:hover { filter: brightness(0.9); }
  .reset { background: #fcacac; }
  .save { background: #a9f6b4; }
  .white { background: #fff; }
  .box {background: #eeeeee;}

  .status {
    text-align: center; 
  }
</style>
