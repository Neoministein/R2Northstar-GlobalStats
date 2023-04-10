import { Column } from 'primereact/column';
import TopService from '../../../demo/service/TopService';
import TopResultTable from '../../../demo/components/TopResultTable';

const NpcKillPage = () => {

    return (
        <TopResultTable 
            title="Top Npc Kills" 
            getGlobalRanking={(tags) => TopService.getTopNpcKills(tags)}
            columns={
                [<Column field="playerName" header="PlayerName"/>,
                <Column field="PGS_NPC_KILLS" header="Npc Kills"/>]}/> 
    );
};

export default NpcKillPage;