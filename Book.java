class Book implements Comparable<Book> {
    private String title;
    private String author;
    private int year;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public static Book createFromString(String line) {

        String[] parts = line.split(","); // assumes no , in the title or author
        if (parts.length != 3)
            throw new IllegalArgumentException("Invalid input");
        int year;
        try {
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input");
        }
        return new Book(parts[0], parts[1], year);

    }

    @Override
    public String toString() {
        return title + " by " + author + " published in " + year;
    }

    @Override
    public int compareTo(Book other) {
        return this.getTitle().compareTo(other.getTitle());
    }

}
