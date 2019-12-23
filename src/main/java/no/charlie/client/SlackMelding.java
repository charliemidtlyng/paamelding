package no.charlie.client;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SlackMelding {
    private final List<SlackBlock> blocks;

    public SlackMelding(List<String> tekster) {
        blocks = tekster.stream()
                .filter(Objects::nonNull)
                .map(SlackBlock::new)
                .collect(Collectors.toList());
    }

    public SlackMelding(String... tekster) {
        blocks = Arrays.stream(tekster)
                .filter(Objects::nonNull)
                .map(SlackBlock::new)
                .collect(Collectors.toList());
    }

    public List<SlackBlock> getBlocks() {
        return blocks;
    }

    private class SlackBlock {
        private final String type = "section";
        private final SlackBlockText text;

        public SlackBlock(String tekst) {
            text = new SlackBlockText(tekst);
        }

        public String getType() {
            return type;
        }

        public SlackBlockText getText() {
            return text;
        }
    }

    private class SlackBlockText {
        private final String type = "mrkdwn";
        private final String text;

        public SlackBlockText(String tekst) {
            this.text = tekst;
        }

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }
    }

}
