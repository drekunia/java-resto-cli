# Java Resto CLI

A small Java command-line app for managing a restaurant menu and taking orders. The terminal interface is in Indonesian. You can add food and drinks, apply a percentage discount to an order, and save or reopen receipts.

I originally built this for a college assignment and kept it as a small Java reference to revisit later.

## Run locally

With JDK 11 or newer, run these commands from the project directory.

```sh
javac -d out Main.java
java -cp out Main
```

The app starts with a sample menu if `data/menu.txt` is missing. Menu changes can be saved to that file, and receipts are stored in `data/struk/`.

`MenuItem` and its subclasses show how the menu is modeled. `Pesanan` handles order totals, and `FileManager` reads and writes the text files.

## License

[MIT](LICENSE)
