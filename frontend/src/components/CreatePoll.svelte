<script>
  let question = '';
  let optionCaptions = ['',''];
  let status = '';
  const creatorId = 1;

  function addOption(){ optionCaptions = [...optionCaptions, '']; }
  function reset(){ question=''; optionCaptions=['','']; }

  async function submit(){
    const clean = optionCaptions.map(s=>s.trim()).filter(Boolean);
    if(!question.trim() || clean.length < 2){ status='Need question + ≥2 options'; return; }

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

    status = 'Created';
    reset();
  }
</script>
<div style="text-align:center; margin-bottom:1rem;">
<h2>Create Poll</h2>
<input placeholder="Question" bind:value={question} />
{#each optionCaptions as _, i}
  <input placeholder={`Option ${i+1}`} bind:value={optionCaptions[i]} />
{/each}
<button on:click={addOption}>Add option</button>
<button on:click={submit}>Save</button>
<p>{status}</p>
</div>
