<script>
  let question = '';
  let options = ['', ''];

  function addOption() {
    options = [...options, ''];
  }

  function nothing() {

  };

   function resetForm() {
    question = '';
    options = ['', ''];
  }

  function submit() {
    const clean = options.map(o => o.trim()).filter(Boolean);
    if (!question.trim() || clean.length < 2) {
      console.log('Invalid: need question + ≥2 options');
      return;
    }
    // Step 2: just log the payload
    console.log('CREATE POLL (stub):', { question, options: clean });
    // reset
    question = ''; options = ['', ''];
  }
</script>

<div style="text-align:center;">
<h2>Create Poll</h2>

<input placeholder="Question" bind:value={question} />

{#each options as _, i}
  <div>
    <input placeholder={`Option ${i+1}`} bind:value={options[i]} />
  </div>
{/each}

<button on:click={addOption}>Add option</button> <br><br>
<button on:click={resetForm} type="button">Reset</button>
<button on:click={submit}>Save</button>

</div>
