package boardgames.logic.model;

import boardgames.shared.dto.Participant;

import java.util.ArrayList;
import java.util.List;

public class Participants {
    public static Participant getById(Iterable<Participant> p, int id) {
        Participant ret = null;
        for (Participant it : p) {
            if (it.participantId() == id) {
                ret = it;
                break;
            }
        }
        return ret;
    }

    public static int countByStatus(Iterable<Participant> p, int status) {
        int ret = 0;
        for (Participant it : p) {
            if (it.status() == status) {
                ret++;
            }
        }
        return ret;
    }

    public static List<Participant> withoutStatus(Iterable<Participant> ps, int status) {
        List<Participant> ret = new ArrayList<>();
        for (Participant p : ps) {
            if (p.status() != status) {
                ret.add(p);
            }
        }
        return ret;
    }
}
