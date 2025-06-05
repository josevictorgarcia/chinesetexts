import { Collection } from "./collection";
import { Text } from "./text";
import { Word } from "./word";

export interface Flashcard {
    id?: number;
    word: Word;
    example: Text;
    collection: Collection;
}