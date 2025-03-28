function JournalEntry(props) {
    return (
        <div>
            <h2>{props.title}</h2>
            <p>{props.content}</p>
        </div>
    );
}

export default JournalEntry;
