<script>
  // Step 2 stubs; later replace with fetch('/api/polls')
  let polls = [
    { id: '1', question: 'Pineapple on pizza?', options: [
      { id: 'a', text: 'Yes', votes: 2 },
      { id: 'b', text: 'No',  votes: 3 }
    ]},
    { id: '2', question: 'Tabs or spaces?', options: [
      { id: 'a', text: 'Tabs',   votes: 5 },
      { id: 'b', text: 'Spaces', votes: 4 }
    ]}
  ];

  let phase = 'pick';
  let selectedPollId = '';
  $: selectedPoll = polls.find(p => p.id === selectedPollId);

  function choose(id) {
    selectedPollId = id;
    phase = 'vote';
  }

  function back() {
    phase = 'pick';
    selectedPollId = '';
  }

  function submitVote(optionId) {
    if (!optionId || !selectedPoll) return;
    console.log('VOTE (stub):', { pollId: selectedPoll.id, optionId });
  }
</script>

<div style="text-align:center;">
  {#if phase === 'pick'}
    <h2>Select a poll</h2>
    <ul style="list-style:none; padding:0;">
      {#each polls as p (p.id)}
        <li>
          {p.question}
          <button on:click={() => choose(p.id)}>choose</button>
        </li>
      {/each}
    </ul>
  {/if}

  {#if phase === 'vote'}
    <h2>{selectedPoll.question}</h2>
    <ul style="list-style:none; padding:0;">
      {#each selectedPoll.options as opt (opt.id)}
        <li>
          <label>
            {opt.text}
            <button on:click={() => submitVote(opt.id)}>vote!</button>
            <span> -- {(opt.votes ?? 0)} {(opt.votes === 1 ? 'vote' : 'votes')}</span>
          </label>
        </li>
      {/each}
    </ul>
    <button on:click={back}>← Back</button>
  {/if}
</div>
